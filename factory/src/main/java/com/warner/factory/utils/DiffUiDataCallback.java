package com.warner.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by warner on 2018/1/26.
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldlist, mNewList;

    public DiffUiDataCallback(List<T> oldlist, List<T> newList) {
        mOldlist = oldlist;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldlist.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    // 两个类是否是同一个东西，如ID相同的User
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldlist.get(oldItemPosition);
        T beanNew = mOldlist.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }


    // 经过相等判断后，进一步判断是否有数据更改
    // 同一用户的两个不同实例，其中name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldlist.get(oldItemPosition);
        T beanNew = mOldlist.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    // 进行比较的数据类型
    // 泛型的目的，你和与你同一类型的数据进行比较
    public interface UiDataDiffer<T> {
        // 传递一个旧的数据给你，问你是否和你表示的是同一类型
        boolean isSame(T old);
        // 你和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
