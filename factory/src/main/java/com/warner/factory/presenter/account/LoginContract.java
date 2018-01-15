package com.warner.factory.presenter.account;

import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/15.
 */

public interface LoginContract {

    interface View extends BaseContract.View<Presenter> {
        // 登录成功
        void loginSuccess();
    }



    interface Presenter extends BaseContract.Presenter {
        // 发起一个登录
        void login(String phone, String password);
    }
}
