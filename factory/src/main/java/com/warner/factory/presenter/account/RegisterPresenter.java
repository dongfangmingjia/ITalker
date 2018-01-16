package com.warner.factory.presenter.account;

import android.text.TextUtils;

import com.warner.common.Common;
import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.AccountHelper;
import com.warner.factory.model.api.account.RegisterModel;
import com.warner.factory.model.db.User;
import com.warner.factory.persistence.Account;
import com.warner.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * Created by warner on 2018/1/15.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter, DataSource.Callback<User> {


    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        // 调用开始方法，在start中默认启动了Loading
        start();

        // 得到View接口
        RegisterContract.View view = getView();

        if (!checkMobile(phone)) {
            // 提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2){
            // 姓名需大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            // 密码需大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            // 进行网络请求

            // 构造model，进行请求调用
            RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User user) {
        // 注册成功
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        // 网络就回送，不一定在主线程中，需要强制切回主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDatanotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        // 网络就回送，不一定在主线程中，需要强制切回主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
