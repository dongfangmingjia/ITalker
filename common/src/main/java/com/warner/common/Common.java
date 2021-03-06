package com.warner.common;

/**
 * Created by warner on 2018/1/15.
 */

public class Common {

    /**
     * 一些不可变的永恒的参数
     * 通常用于一些配置
     */
    public interface Constance {
        // 手机号的正则
        String REGEX_MOBILE = "[1][1,4,5,7,8][0-9]{9}$";

        // 基础网络请求地址
        String API_URL = "http://10.2.0.36:8081/api/";
    }
}
