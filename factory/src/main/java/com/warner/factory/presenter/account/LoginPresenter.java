package com.warner.factory.presenter.account;

import android.text.TextUtils;

import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.AccountHelper;
import com.warner.factory.model.api.account.LoginModel;
import com.warner.factory.model.db.User;
import com.warner.factory.persistence.Account;
import com.warner.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by warner on 2018/1/16.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter, DataSource.Callback<User> {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        LoginContract.View view = getView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();

        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDatanotAvailable(final int strRes) {
        final LoginContract.View view = getView();

        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
