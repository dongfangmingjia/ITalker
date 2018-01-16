package com.warner.italker.fragment.account;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.warner.common.app.PresenterFragment;
import com.warner.factory.presenter.account.LoginContract;
import com.warner.factory.presenter.account.LoginPresenter;
import com.warner.italker.R;
import com.warner.italker.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter> implements LoginContract.View {

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;


    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getActivity());
        getActivity().finish();
    }

    @OnClick({R.id.txt_go_register, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_go_register:
                mAccountTrigger.triggerView();
                break;
            case R.id.btn_submit:
                String phone = mPhone.getText().toString();
                String password = mPassword.getText().toString();
                mPresenter.login(phone, password);
                break;
        }
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 停止loading
        mLoading.stop();
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }


    @Override
    public void showLoading() {
        super.showLoading();

        // 停止loading
        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }
}
