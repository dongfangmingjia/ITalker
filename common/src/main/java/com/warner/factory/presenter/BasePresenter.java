package com.warner.factory.presenter;

/**
 * Created by warner on 2018/1/15.
 */

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {


    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /**
     * 设置一个View，子类可以复写
     * @param view
     */
    private void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取View的操作
     * @return
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        T view  = mView;

        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void destory() {
        T view  = mView;
        mView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}
