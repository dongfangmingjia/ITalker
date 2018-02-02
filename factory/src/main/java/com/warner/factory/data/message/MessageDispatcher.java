package com.warner.factory.data.message;

import android.text.TextUtils;

import com.warner.factory.data.helper.DBHelper;
import com.warner.factory.data.helper.GroupHelper;
import com.warner.factory.data.helper.MessageHelper;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.model.card.MessageCard;
import com.warner.factory.model.db.Group;
import com.warner.factory.model.db.Message;
import com.warner.factory.model.db.User;
import com.warner.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by warner on 2018/2/2.
 */

public class MessageDispatcher implements MessageCenter {



    @Override
    public void dispatch(MessageCard... messageCards) {
        if (messageCards == null || messageCards.length == 0) {
            return;
        }

        mExecutor.execute(new MessageCardHandler(messageCards));
    }

    private static MessageCenter instance;
    // 单线程池，处理卡片一个个的消息
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public static MessageCenter getInstance() {
        if (instance == null) {
            synchronized (MessageDispatcher.class){
                if (instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }

    /**
     * 线程调度
     */
    private class MessageCardHandler implements Runnable {

        private final MessageCard[] mCards;


        MessageCardHandler(MessageCard[] cards) {
            this.mCards = cards;
        }

        @Override
        public void run() {
            // 单线程调度的时候触发
            ArrayList<Message> messages = new ArrayList<>();
            for (MessageCard card: mCards) {
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getReceiverId())
                        || (TextUtils.isEmpty(card.getId()) && TextUtils.isEmpty(card.getGroupId()))) {
                    continue;
                }

                // 消息卡片有可能是推送过来的，也有可能是直接造的
                // 推送来的代表服务器有，我们可以查询到（本地有可能有，有可能没有）
                // 如果是直接造的，那么先存本地，后发送网络
                // 发送消息流程：写消息->存储本地->发送网络->网络返回->刷新本地
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {
                    // 如果本地消息显示已经完成则不做处理
                    if (message.getStatus() == Message.STATUS_DONE) {
                        continue;
                    }
                    // 新状态为完成才更新服务器时间，不然不做更新
                    if (card.getStatus() == Message.STATUS_DONE) {
                        // 网络发送成功，修改时间为服务器时间
                        message.setCreateAt(card.getCreateAt());
                    }
                    // 更新变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {
                    // 没有找到本地消息，初次在数据库存储
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }

                    if (sender != null && receiver == null && group == null) {
                        continue;
                    }

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);

                if (messages.size() > 0) {
                    DBHelper.save(Message.class, CollectionUtil.toArray(messages, Message.class));
                }
            }
        }
    }
}
