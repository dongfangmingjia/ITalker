package com.warner.factory.presenter.account;

import android.text.TextUtils;

import com.warner.common.Common;
import com.warner.factory.data.helper.AccountHelper;
import com.warner.factory.model.api.account.RegisterModel;
import com.warner.factory.presenter.BasePresenter;

import java.util.regex.Pattern;

/**
 * Created by warner on 2018/1/15.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {


    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        // 调用开始方法，在start中默认启动了Loading
        start();

        if (!checkMobile(phone)) {
            // 提示

        } else if (name.length() < 2){
            // 姓名需大于2位

        } else if (password.length() < 6) {
            // 密码需大于6位

        } else {
            // 进行网络请求

            // 构造model，进行请求调用
            RegisterModel model = new RegisterModel(phone, password, name);
            AccountHelper.register(model);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }
}
