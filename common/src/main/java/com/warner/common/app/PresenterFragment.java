package com.warner.common.app;

import android.content.Context;

import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/15.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends BaseFragment implements BaseContract.View<Presenter> {


    protected Presenter mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 在界面onAttach之后就触发初始化Presenter
        initPresenter();
    }

    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        BaseApplication.showToast(str);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
