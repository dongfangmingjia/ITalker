package com.warner.factory.model.api.account;

import com.warner.factory.model.db.User;

/**
 * Created by warner on 2018/1/16.
 */

public class AccountRspModel {

    private User mUser;

    private String account;

    private String token;

    private boolean isBind;


    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "mUser=" + mUser +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
