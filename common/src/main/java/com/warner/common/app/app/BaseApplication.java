package com.warner.common.app.app;

import android.app.Application;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

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


    public static Application getInstance() {
        return instance;
    }


    /**
     * 获取缓存文件夹地址
     * @return
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**获取头像的临时存储文件地址*/
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

    /**
     * 获取声音文件的本地地址
     * @param isTmp 是否是缓存文件
     * @return
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        dir.mkdirs();

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * 显示一个toast
     * @param msg
     */
    public static void showToast(final String msg) {
//        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示一个toast
     * @param msgId
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }
}
