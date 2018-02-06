package com.warner.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.warner.common.widget.recycler.RecyclerAdapter;
import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.data.user.ContactDataSource;
import com.warner.factory.data.user.ContactRepository;
import com.warner.factory.model.db.User;
import com.warner.factory.presenter.BaseSourcePresenter;
import com.warner.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by warner on 2018/1/25.
 */

public class ContactPresenter extends BaseSourcePresenter<User, ContactDataSource, User, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SuccessCallback<List<User>> {


    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据
        UserHelper.refreshContacts();
    }

    @Override
    public void onDataLoaded(List<User> users) {
        ContactContract.View view = getView();
        if (view != null) {
            RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
            List<User> oldList = adapter.getItems();

            // 进行数据对比
            DiffUiDataCallback<User> callback = new DiffUiDataCallback<>(oldList, users);

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
            // 进行界面刷新
            refreshData(result, users);
        }
    }
}
