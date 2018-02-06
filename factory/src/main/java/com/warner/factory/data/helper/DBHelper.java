package com.warner.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.warner.factory.model.db.AppDatabase;
import com.warner.factory.model.db.Group;
import com.warner.factory.model.db.GroupMember;
import com.warner.factory.model.db.Group_Table;
import com.warner.factory.model.db.Message;
import com.warner.factory.model.db.Session;
import com.warner.utils.CollectionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库存储
 * <p>
 * Created by warner on 2018/2/2.
 */

public class DBHelper {

    public static final DBHelper instance;

    static {
        instance = new DBHelper();
    }

    private DBHelper() {
    }

    /**
     * 观察者集合
     * Class<?>：观察的表
     * Set<ChangedListener>：每一个表对应的观察者有很多
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 获取某个表的所有监听者
     *
     * @param modelClass 表对应的Class信息
     * @param <Model>
     * @return
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   对某表的关注
     * @param listener 监听者
     * @param <Model>
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> listeners = instance.getListeners(tClass);
        if (listeners == null) {
            // 初始化某一类型的容器
            listeners = new HashSet<>();
            // 添加到Map
            instance.changedListeners.put(tClass, listeners);
        }

        listeners.add(listener);
    }

    /**
     * 删除一个监听者
     *
     * @param tClass   对某表的关注
     * @param listener 监听者
     * @param <Model>
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> listeners = instance.getListeners(tClass);
        if (listeners == null) {
            // 容器本身为null，代表根本没有
            return;
        }
        // 从容器中删除
        listeners.remove(listener);
    }

    /**
     * 进行数据库的增加、更改操作
     *
     * @param tClass
     * @param models
     * @param <Model>
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.saveAll(Arrays.asList(models));
                instance.notifySave(tClass, models);
            }
        });
    }


    /**
     * 进行数据库的删除操作
     *
     * @param tClass
     * @param models
     * @param <Model>
     */
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(Arrays.asList(models));
                instance.notifyDelete(tClass, models);
            }
        });
    }


    /**
     * 通知保存
     *
     * @param tClass
     * @param models
     * @param <Model>
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        // 找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }

        // 群成员变更需要通知对应群信息更新
        if (Group.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            // 消息变更通知会话列表更新
            updateSession((Message[]) models);
        }
    }


    /**
     * 通知删除
     *
     * @param tClass
     * @param models
     * @param <Model>
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        // 找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }

        // 群成员变更需要通知对应群信息更新
        if (Group.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            // 消息变更通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出对应的群，并对群进行更新
     *
     * @param members 群成员列表
     */
    private void updateGroup(GroupMember... members) {

        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            // 添加群Id
            groupIds.add(member.getGroup().getId());
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> groupList = SQLite.select().from(Group.class).where(Group_Table.id.in(groupIds)).queryList();
                instance.notifySave(Group.class, CollectionUtil.toArray(groupList, Group.class));
            }
        }).build().execute();
    }

    /**
     * 从消息列表中找出对应的消息，并对消息进行更新
     *
     * @param messages 消息列表
     */
    private void updateSession(Message... messages) {
        // 标示一个Session的唯一性
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;

                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);
                    
                    if (session == null) {
                        // 第一次聊天，创建一个你和对方的会话
                        session = new Session(identify);
                    }

                    // 把会话刷新到当前Message 的最新状态
                    session.refreshToNow();
                    // 数据存储
                    adapter.save(session);
                    // 添加到集合
                    sessions[index++] = session;
                }
                instance.notifySave(Session.class, sessions);
            }
        }).build().execute();
    }

    /**
     * 通知监听器
     *
     * @param <Data>
     */
    public interface ChangedListener<Data extends BaseModel> {
        /**
         * 数据保存的更新
         */
        void onDataSave(Data... list);

        /**
         * 数据删除的更新
         */
        void onDataDelete(Data... list);
    }
}

