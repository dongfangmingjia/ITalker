package com.warner.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.warner.common.app.BaseActivity;
import com.warner.factory.model.Author;
import com.warner.factory.model.db.Group;
import com.warner.italker.R;
import com.warner.italker.fragment.message.ChatGroupFragment;
import com.warner.italker.fragment.message.ChatUserFragment;

public class MessageActivity extends BaseActivity {
    // 传递一个接收者的id
    public static final String KEY_RECEIVER_ID = "key_receiver_id";
    // 标示是否是一个群
    private static final String KEY_RECEIVER_IS_GROUP = "key_receiver_is_group";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示人的聊天界面
     * @param context
     * @param author
     */
    public static void show(Context context, Author author) {
        if (context == null || author == null || TextUtils.isEmpty(author.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 显示群的聊天界面
     * @param context
     * @param group
     */
    public static void show(Context context, Group group) {
        if (context == null || group == null || TextUtils.isEmpty(group.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);

        return !TextUtils.isEmpty(mReceiverId);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }

        // 从Activity传递参数到Fragment中
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, fragment).commit();
    }
}
