package com.warner.italker.fragment.message;


import android.support.v4.app.Fragment;

import com.warner.factory.model.db.Group;
import com.warner.factory.presenter.message.ChatContract;
import com.warner.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
