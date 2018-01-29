package com.warner.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.warner.common.R;

/**
 * Created by warner on 2018/1/17.
 */

public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();

        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initTitleNeedBack();
    }


    protected void initTitleNeedBack() {
        // 设置左上角返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
