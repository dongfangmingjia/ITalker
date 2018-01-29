package com.warner.factory.data.helper;

import com.warner.factory.Factory;
import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.api.user.UserUpdateModel;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.model.db.User;
import com.warner.factory.net.Network;
import com.warner.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by warner on 2018/1/17.
 */

public class UserHelper {

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
                    User user = userCard.build();
                    user.save();
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

    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback) {
        RemoteService remote = Network.remote();
        Call<RspModel<List<UserCard>>> call = remote.userContacts();
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                if (response != null) {
                    RspModel<List<UserCard>> rspModel = response.body();
                    if (rspModel != null && rspModel.success()) {
                        callback.onDataLoaded(rspModel.getResult());
                    } else {
                        Factory.decodeRspCode(rspModel, callback);
                    }
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDatanotAvailable(R.string.data_network_error);
            }
        });
    }
}
