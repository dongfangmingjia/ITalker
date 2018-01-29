package com.warner.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.warner.factory.model.db.User;
import com.warner.factory.presenter.BasePresenter;
import com.warner.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by warner on 2018/1/25.
 */

public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();

        // 加载数据

    }


    private void diff(List<User> newList, List<User> oldList) {

        DiffUiDataCallback<User> callback = new DiffUiDataCallback<>(oldList, newList);

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
    }
}
