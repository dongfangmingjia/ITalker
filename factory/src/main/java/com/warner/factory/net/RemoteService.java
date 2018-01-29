package com.warner.factory.net;

import com.warner.factory.model.api.RspModel;
import com.warner.factory.model.api.account.AccountRspModel;
import com.warner.factory.model.api.account.LoginModel;
import com.warner.factory.model.api.account.RegisterModel;
import com.warner.factory.model.api.user.UserUpdateModel;
import com.warner.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by warner on 2018/1/16.
 */

public interface RemoteService {

    /**
     * 注册接口
     * @param model
     * @return
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     * @param model
     * @return
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);


    /**
     * 绑定设备ID
     * @param pushId
     * @return
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户更新信息
     * @param model
     * @return
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    /**
     * 搜索联系人
     * @param name
     * @return
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> searchUser(@Path("name") String name);

    /**
     * 关注联系人
     * @param userId
     * @return
     */
    @GET("user/follow/{userId}")
    Call<RspModel<UserCard>> followUser(@Path("userId") String userId);

    /**
     * 获取联系人列表
     * @return
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();
}
