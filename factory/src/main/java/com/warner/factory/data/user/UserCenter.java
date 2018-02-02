package com.warner.factory.data.user;

import com.warner.factory.model.card.UserCard;

/**
 * Created by warner on 2018/2/2.
 */

public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}
