package com.warner.factory.presenter;

import android.support.annotation.StringRes;

import com.warner.common.widget.recycler.RecyclerAdapter;

/**
 * 最基础的契约接口，每个页面模块都应该继承这个接口，定义自己的view及presenter
 *
 * Created by warner on 2018/1/15.
 */

public interface BaseContract {

    // 基本View的职责
    interface View<T extends Presenter> {
        // 公共的显示一个错误信息
        void showError(@StringRes int str);

        // 公共的显示进度条
        void showLoading();

        // 设置一个Presenter
        void setPresenter(T presenter);
    }


    // 基本的Presenter的职责
    interface Presenter{
        // 共用的开始触发
        void start();

        // 共用的销毁触发
        void destory();
    }

    // 基本列表的职责
    interface RecyclerView<T extends Presenter, ViewModel> extends View<T> {

        RecyclerAdapter<ViewModel> getRecyclerAdapter();

        // 当适配器数据更改了的时候触发
        void onAdapterDataChanged();
    }
}
