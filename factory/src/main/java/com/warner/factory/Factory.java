package com.warner.factory;

import com.warner.common.app.BaseApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by warner on 2018/1/10.
 */

public class Factory {

    private static final Factory instance;
    private final Executor mExecutor;

    static {
        instance = new Factory();
    }

    private Factory() {
        mExecutor = Executors.newFixedThreadPool(4);
    }


    public static BaseApplication app() {
        return BaseApplication.getInstance();
    }

    /**
     * 异步执行
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        instance.mExecutor.execute(runnable);
    }
}
