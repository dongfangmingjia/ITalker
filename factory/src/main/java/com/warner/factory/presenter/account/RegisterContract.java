package com.warner.factory.presenter.account;

import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/15.
 */

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter> {
        // 注册成功
        void registerSuccess();
    }



    interface Presenter extends BaseContract.Presenter {
        // 发起一个注册
        void register(String phone, String name, String password);

        // 检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
