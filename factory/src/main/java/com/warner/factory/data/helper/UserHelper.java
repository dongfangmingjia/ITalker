package com.warner.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.warner.factory.Factory;
import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.api.user.UserUpdateModel;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.model.db.User;
import com.warner.factory.model.db.User_Table;
import com.warner.factory.net.Network;
import com.warner.factory.net.RemoteService;
import com.warner.utils.CollectionUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by warner on 2018/1/17.
 */

public class UserHelper {

    private Response<RspModel<List<UserCard>>> response;

    /**
     * 更新用户信息
     * @param model
     * @param callback
     */
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService remote = Network.remote();
        Call<RspModel<UserCard>> call = remote.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDatanotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 搜索用户
     * @param name
     * @param callback
     */
    public  static Call searchUser(String name, final DataSource.Callback<List<UserCard>> callback) {

        RemoteService remote = Network.remote();
        Call<RspModel<List<UserCard>>> call = remote.searchUser(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<UserCard> userCards = rspModel.getResult();
                    callback.onDataLoaded(userCards);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDatanotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }


    public static void followUser(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService remote = Network.remote();
        Call<RspModel<UserCard>> call = remote.followUser(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                if (response != null) {
                    RspModel<UserCard> rspModel = response.body();
                    if (rspModel != null && rspModel.success()) {
                        callback.onDataLoaded(rspModel.getResult());
                    } else {
                        Factory.decodeRspCode(rspModel, callback);
                    }
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDatanotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 刷新联系人的操作，不需要CallBack，直接存储数据库，并通过数据库观察者进行通知界面更新
     * 界面更新的时候进行对比，然后差异更新
     */
    public static void refreshContacts() {
        RemoteService remote = Network.remote();
        Call<RspModel<List<UserCard>>> call = remote.userContacts();
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                if (response != null) {
                    RspModel<List<UserCard>> rspModel = response.body();
                    if (rspModel != null && rspModel.success()) {
                        List<UserCard> userCards = rspModel.getResult();
                        if (userCards == null || userCards.size() == 0) {
                            return;
                        }
                        Factory.getUserCenter().dispatch(CollectionUtil.toArray(userCards, UserCard.class));
                    } else {
                        Factory.decodeRspCode(rspModel, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {

            }
        });
    }

    /**
     * 从本地查询一个用户的信息
     * @param id
     * @return
     */
    public static User findFromLocal(String id) {
        return SQLite.select().from(User.class).where(User_Table.id.eq(id)).querySingle();
    }

    /**
     * 从网络查询某用户的信息
     * @param id
     * @return
     */
    public static User findFromNet(String id) {
        RemoteService remote = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remote.userFind(id).execute();
            UserCard userCard = response.body().getResult();
            if (userCard != null) {
                Factory.getUserCenter().dispatch(userCard);
                return userCard.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 搜索一个用户，优先本地缓存，没有用然后再从网络拉取
     * @param id
     * @return
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络查询，没有然后再从本地拉取
     * @param id
     * @return
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }
}
