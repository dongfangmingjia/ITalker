package com.warner.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.warner.common.app.BaseActivity;
import com.warner.italker.R;
import com.warner.italker.fragment.user.UpdateInfoFragment;

public class UserActivity extends BaseActivity {

    private Fragment mCurFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
