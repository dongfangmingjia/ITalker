package com.warner.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by warner on 2018/1/16.
 */

public interface DataSource {

    interface Callback<T> extends SuccessCallback<T>, FailedCallback {

    }

    interface SuccessCallback<T> {
        // 数据加载成功，网络请求成功
        void onDataLoaded(T t);
    }


    interface FailedCallback {
        // 数据加载失败，网络请求失败
        void onDatanotAvailable(@StringRes int strRes);
    }

    /**
     * 销毁操作
     */
    void dispose();
}
