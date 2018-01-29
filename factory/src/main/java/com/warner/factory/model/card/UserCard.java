package com.warner.factory.model.card;

import com.warner.factory.model.Author;
import com.warner.factory.model.db.User;

import java.util.Date;

/**
 * Created by warner on 2018/1/17.
 */

public class UserCard implements Author {

    private String id;
    private String name;
    private String phone;
    private String portrait;
    private String desc;
    private int sex = 0;
    // 备注信息
    private String alias;
    // 关注人的数量
    private int follows;
    // 粉丝数量
    private int following;
    // 是否关注
    private boolean isFollow;
    // 时间字段
    private Date modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    // 不能被Json解析使用
    private transient User mUser;

    public User build() {
        if (mUser == null) {
            mUser = new User();
            mUser.setId(id);
            mUser.setName(name);
            mUser.setPhone(phone);
            mUser.setPortrait(portrait);
            mUser.setDesc(desc);
            mUser.setSex(sex);
            mUser.setAlias(alias);
            mUser.setFollows(follows);
            mUser.setFollowing(following);
            mUser.setFollow(isFollow);
            mUser.setModifyAt(modifyAt);
        }
        return mUser;
    }
}
