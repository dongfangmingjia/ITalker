package com.warner.factory.net;

import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.warner.factory.Factory;
import com.warner.utils.HashUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by warner on 2018/1/10.
 */

public class UploadHelper {

    private static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
    private static final String BUCKET_NAME = "italker-new";

    private static OSS getClient() {
        OSSPlainTextAKSKCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIYQD07p05pHQW", "2txxzT8JXiHKEdEjylumFy6sXcDQ0G");
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传文件
     * @param objKey
     * @param path
     * @return
     */
    public static String upload(String objKey, String path) {
        // 构造一个上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);

        try {
            // 初始化上传的Client
            OSS client = getClient();
            // 开始同步上传
            PutObjectResult result = client.putObject(request);
            // 获取一个外网可以访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分月存储，避免文件夹太多
     * @return
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * 获取图片存储的key
     * @param path
     * @return
     */
    private static final String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 获取头像存储的key
     * @param path
     * @return
     */
    private static final String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 获取音频存储的key
     * @param path
     * @return
     */
    private static final String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }


    /**
     * 上传图片
     * @param path
     * @return
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     * @param path
     * @return
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }


    /**
     * 上传音频
     * @param path
     * @return
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }
}
