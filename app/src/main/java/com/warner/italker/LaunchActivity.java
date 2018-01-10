package com.warner.italker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.warner.common.app.BaseActivity;
import com.warner.italker.activities.MainActivity;
import com.warner.italker.fragment.assist.PermissionsFragment;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void initWidget() {
        super.initWidget();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionsFragment.hasAllPerm(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}
