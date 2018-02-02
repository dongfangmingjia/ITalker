package com.warner.factory.data.user;

import android.text.TextUtils;

import com.warner.factory.data.helper.DBHelper;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.model.db.User;
import com.warner.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by warner on 2018/2/2.
 */

public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    // 单线程池，处理卡片一个个的消息
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public static UserCenter getInstance() {
        if (instance == null) {
            synchronized (UserDispatcher.class){
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
         }
         return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        mExecutor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度
     */
    private class UserCardHandler implements Runnable {

        private final UserCard[] mCards;


        UserCardHandler(UserCard[] cards) {
            this.mCards = cards;
        }

        @Override
        public void run() {
            // 单线程调度的时候触发
            ArrayList<User> users = new ArrayList<>();
            for (UserCard card: mCards) {
                // 进行过滤
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.build());
            }
            // 进行数据库存储，并分发通知，异步操作
            DBHelper.save(User.class, CollectionUtil.toArray(users, User.class));
        }
    }
}
