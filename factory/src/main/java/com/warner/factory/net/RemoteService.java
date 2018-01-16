package com.warner.factory.net;

import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.api.account.AccountRspModel;
import com.warner.factory.model.api.account.LoginModel;
import com.warner.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by warner on 2018/1/16.
 */

public interface RemoteService {

    /**
     * 网络请求一个注册接口
     * @param model
     * @return
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);


    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);



    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);
}
