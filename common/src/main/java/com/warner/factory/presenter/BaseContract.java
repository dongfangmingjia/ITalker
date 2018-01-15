package com.warner.factory.presenter;

import android.support.annotation.StringRes;

/**
 * Created by warner on 2018/1/15.
 */

public interface BaseContract {

    interface View<T extends Presenter> {
        // 公共的显示一个错误信息
        void showError(@StringRes int str);

        // 公共的显示进度条
        void showLoading();

        // 设置一个Presenter
        void setPresenter(T presenter);
    }



    interface Presenter{
        // 共用的开始触发
        void start();

        // 共用的销毁触发
        void destory();
    }
}
