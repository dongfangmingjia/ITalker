package com.warner.factory.data.helper;

import android.text.TextUtils;

import com.warner.factory.Factory;
import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.api.account.AccountRspModel;
import com.warner.factory.model.api.account.LoginModel;
import com.warner.factory.model.api.account.RegisterModel;
import com.warner.factory.model.db.User;
import com.warner.factory.net.Network;
import com.warner.factory.net.RemoteService;
import com.warner.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by warner on 2018/1/15.
 */

public class AccountHelper {

    /**
     * 注册
     * @param model
     * @param callback
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        // 调用Re
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> rspModelCall = service.accountRegister(model);
        rspModelCall.enqueue(new AccountCallback(callback));
    }

    /**
     * 绑定设备ID
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }

        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountCallback(callback));
    }

    /**
     * 登录
     * @param model
     * @param callback
     */
    public static void login(LoginModel model, DataSource.Callback<User> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountCallback(callback));
    }



    public static class AccountCallback implements Callback<RspModel<AccountRspModel>> {

        DataSource.Callback<User> callback;

        AccountCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            // 请求成功
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                // 获取实体
                AccountRspModel accountRspModel = rspModel.getResult();

                User user = accountRspModel.getUser();
                // 进行数据库写入和缓存绑定
                user.save();
                Account.login(accountRspModel);

                // 判断绑定状态，是否绑定设备
                if (accountRspModel.isBind()) {
                    Account.setBind(true);
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                } else {
                    // 进行绑定
                    bindPush(callback);
                }
            } else {
                // 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源上
                Factory.decodeRspCode(rspModel, callback);
            }

        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            // 网络请求失败
            if (callback != null) {
                callback.onDatanotAvailable(R.string.data_network_error);
            }
        }
    }
}
