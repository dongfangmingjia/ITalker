package com.warner.factory.net;

import android.text.TextUtils;

import com.warner.common.Common;
import com.warner.factory.Factory;
import com.warner.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by warner on 2018/1/16.
 */

public class Network {

    private static Network instance;
    private Retrofit mRetrofit;


    static{
        instance = new Network();
    }

    public Network() {

    }

    public static Retrofit getRetrofit() {

        if (instance.mRetrofit != null) {
            return instance.mRetrofit;
        }

        // 获取client
        OkHttpClient client = new OkHttpClient.Builder()
                // 为所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 得到请求，重新进行build
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        
                        builder.addHeader("Content_Type", "application/json");
                        Request newRequset = builder.build();
                        return chain.proceed(newRequset);
                    }
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder();

        instance.mRetrofit =  builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))// 设置json解析
                .build();
        return instance.mRetrofit;
    }


    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }
}
