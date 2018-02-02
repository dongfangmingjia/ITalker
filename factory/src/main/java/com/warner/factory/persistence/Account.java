package com.warner.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.warner.factory.Factory;
import com.warner.factory.model.api.account.AccountRspModel;
import com.warner.factory.model.db.User;
import com.warner.factory.model.db.User_Table;

/**
 * 用户账户持续化的类
 *
 * Created by warner on 2018/1/16.
 */

public class Account {

    private static final String KEY_PUSH_ID = "key_push_id";
    private static final String KEY_IS_BIND = "key_is_bind";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_ACCOUNT = "key_account";

    private static String pushId;

    private static boolean mIsBind;

    private static String token;
    private static String userId;
    private static String account;

    private static void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, mIsBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 进行数据加载
     * @param context
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        mIsBind = sp.getBoolean(KEY_IS_BIND, false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
    }

    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    public static String getPushId() {
        return pushId;
    }


    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }

    /**
     * 是否已经完善用户信息
     * @return
     */
    public static boolean isComplete() {
        if (isLogin()) {
            User self = getUser();
            return !TextUtils.isEmpty(self.getDesc())
                    && !TextUtils.isEmpty(self.getPortrait())
                    && self.getSex() != 0;
        }
        return false;
    }

    /**
     * 是否已经绑定服务器
     * @return
     */
    public static boolean isBind() {
        return Account.mIsBind;
    }


    /**
     * 设置绑定状态
     * @param isBind
     */
    public static void setBind(boolean isBind) {
        Account.mIsBind = isBind;
        Account.save(Factory.app());
    }

    /**
     * 保存自己的数据到xml中
     * @param model
     */
    public static void login(AccountRspModel model) {
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();

        save(Factory.app());
    }

    /**
     * 获取当前登录用户的信息
     * @return
     */
    public static User getUser() {
        return TextUtils.isEmpty(userId) ? new User() :
                SQLite.select().from(User.class).where(User_Table.id.eq(userId)).querySingle();
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken() {
        return Account.token;
    }

    public static String getUserId() {
        return Account.userId;
    }
}
