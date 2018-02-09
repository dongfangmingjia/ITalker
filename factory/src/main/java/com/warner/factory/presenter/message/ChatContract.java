package com.warner.factory.presenter.message;

import com.warner.factory.model.db.Group;
import com.warner.factory.model.db.Message;
import com.warner.factory.model.db.User;
import com.warner.factory.presenter.BaseContract;

/**
 * 聊天契约
 *
 * Created by warner on 2018/2/9.
 */

public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        // 发送文字
        void pushText(String content);
        // 发送语音
        void pushAudio(String path);
        // 发送图片
        void pushImages(String[] paths);

        // 重新发送一条消息，返回是否调度成功
        boolean rePush(Message message);
    }


    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        // 初始化Model
        void onInit(InitModel model);
    }


    interface UserView extends View<User> {

    }

    interface GroupView extends View<Group> {

    }
}
