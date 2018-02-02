package com.warner.factory.data.group;

import com.warner.factory.data.helper.DBHelper;
import com.warner.factory.data.helper.GroupHelper;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.model.card.GroupCard;
import com.warner.factory.model.card.GroupMemberCard;
import com.warner.factory.model.db.Group;
import com.warner.factory.model.db.GroupMember;
import com.warner.factory.model.db.User;
import com.warner.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by warner on 2018/2/2.
 */

public class GroupDispatcher implements GroupCenter {

    private static GroupCenter instance;
    // 单线程池，处理卡片一个个的消息
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public static GroupCenter getInstance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class){
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        mExecutor.execute(new GroupCardHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        mExecutor.execute(new GroupMemberHandler(cards));
    }

    /**
     * 线程调度
     */
    private class GroupCardHandler implements Runnable {

        private final GroupCard[] mCards;


        GroupCardHandler(GroupCard[] cards) {
            this.mCards = cards;
        }

        @Override
        public void run() {
            // 单线程调度的时候触发
            ArrayList<Group> groups = new ArrayList<>();
            for (GroupCard card: mCards) {
                User owner = UserHelper.search(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            // 进行数据库存储，并分发通知，异步操作
            if (groups.size() > 0) {
                DBHelper.save(Group.class, CollectionUtil.toArray(groups, Group.class));
            }
        }
    }


    private class GroupMemberHandler implements Runnable {

        private final GroupMemberCard[] cards;

        GroupMemberHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            ArrayList<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard card : cards) {
                User user = UserHelper.search(card.getUserId());
                Group group = GroupHelper.find(card.getGroupId());
                if (user != null && group != null) {
                    GroupMember member = card.build(group, user);
                    members.add(member);
                }
            }

            if (members.size() > 0) {
                DBHelper.save(GroupMember.class, CollectionUtil.toArray(members, GroupMember.class));
            }
        }
    }
}
