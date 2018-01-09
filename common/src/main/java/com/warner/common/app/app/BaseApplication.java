package com.warner.common.app.app;

import android.app.Application;

import java.io.File;

/**
 * Created by warner on 2018/1/9.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取缓存文件夹地址
     * @return
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }


    public static File getPortraitTmpFile() {
        // 获取头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有的对应文件夹
        dir.mkdirs();

        // 删除旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        // 返回一个当前时间戳的目录文件地址
        File path = new File(dir, System.currentTimeMillis() + "jpg");
        return path.getAbsoluteFile();
    }
}
