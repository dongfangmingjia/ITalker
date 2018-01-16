package com.warner.italker;

import com.igexin.sdk.PushManager;
import com.warner.common.app.BaseApplication;
import com.warner.factory.Factory;

/**
 * Created by warner on 2018/1/9.
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();
        // 推送初始化
        PushManager.getInstance().initialize(this);
    }
}
