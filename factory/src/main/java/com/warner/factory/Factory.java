package com.warner.factory;

import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.warner.common.app.BaseApplication;
import com.warner.factory.data.DataSource;
import com.warner.factory.data.group.GroupCenter;
import com.warner.factory.data.group.GroupDispatcher;
import com.warner.factory.data.message.MessageCenter;
import com.warner.factory.data.message.MessageDispatcher;
import com.warner.factory.data.user.UserCenter;
import com.warner.factory.data.user.UserDispatcher;
import com.warner.factory.model.api.PushModel;
import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.card.GroupCard;
import com.warner.factory.model.card.GroupMemberCard;
import com.warner.factory.model.card.MessageCard;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.persistence.Account;
import com.warner.factory.utils.DBFlowExclusionStrategy;
import com.warner.utils.CollectionUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by warner on 2018/1/10.
 */

public class Factory {

    private static final Factory instance;
    // 全局的线程池
    private final Executor mExecutor;
    // 全局的Gson
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    private Factory() {
        mExecutor = Executors.newFixedThreadPool(4);
        mGson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置一个过滤器，数据库级别的Model不进行Json转换
                 .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * 初始化
     */
    public static void setup() {
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)// 数据库初始化的时候就打开
                .build());
        Account.load(app());
    }


    public static BaseApplication app() {
        return BaseApplication.getInstance();
    }

    /**
     * 异步执行
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        instance.mExecutor.execute(runnable);
    }

    /**
     * 返回一个全局的Gson
     * @return
     */
    public static Gson getGson() {
        return instance.mGson;
    }

    /**
     * 进行错误Code的解析
     * 将网络返回的Code值进行统一的规划并返回一个String资源
     *
     * @param model
     * @param callback
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null) {
            return;
        }

        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            default:
            case RspModel.ERROR_UNKNOWN:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                BaseApplication.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId, final DataSource.FailedCallback callback) {
        if (callback != null) {
            callback.onDatanotAvailable(resId);
        }
    }

    private void logout() {

    }

    /**
     * 处理推送来的消息
     * @param message
     */
    public static void dispatchPush(String message) {
        // 首先检查登录状态
        if (!Account.isLogin()) {
            return;
        }
        PushModel model = PushModel.decode(message);
        if (model != null) {
            // 对推送集合进行遍历
            for (PushModel.Entity entity : model.getEntities()) {
                switch (entity.type) {
                    case PushModel.ENTITY_TYPE_LOGOUT:
                        // 退出登录情况下，直接返回，不可继续
                        instance.logout();
                        return;
                    case PushModel.ENTITY_TYPE_MESSAGE:
                        // 普通消息
                        MessageCard messageCard = getGson().fromJson(entity.content, MessageCard.class);
                        getMessageCenter().dispatch(messageCard);
                        break;
                    case PushModel.ENTITY_TYPE_ADD_FRIEND:
                        // 好友添加
                        UserCard userCard = getGson().fromJson(entity.content, UserCard.class);
                        getUserCenter().dispatch(userCard);
                        break;
                    case PushModel.ENTITY_TYPE_ADD_GROUP:
                        // 群添加
                        GroupCard groupCard = getGson().fromJson(entity.content, GroupCard.class);
                        getGroupCenter().dispatch(groupCard);
                        break;
                    case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                    case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS:
                        // 群成员变更
                        TypeToken<List<GroupMemberCard>> typeToken = new TypeToken<List<GroupMemberCard>>() {
                        };
                        Type type = typeToken.getType();
                        List<GroupMemberCard> memberCards = getGson().fromJson(entity.content, type);
                        getGroupCenter().dispatch(CollectionUtil.toArray(memberCards, GroupMemberCard.class));
                        break;
                    case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:
                        // 成员退出

                        break;
                }
            }
        }
    }

    /**
     * 获取用户中心的实现类
     * @return
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.getInstance();
    }

    /**
     * 获取消息中心的实现类
     * @return
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.getInstance();
    }

    /**
     * 获取群的实现类
     * @return
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.getInstance();
    }

}
