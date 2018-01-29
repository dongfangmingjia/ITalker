package com.warner.italker.activities;

import android.content.Context;
import android.content.Intent;

import com.warner.common.app.BaseActivity;
import com.warner.factory.model.Author;
import com.warner.italker.R;

public class MessageActivity extends BaseActivity {

    public static void show(Context context, Author author) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_message;
    }
}
