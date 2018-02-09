package com.warner.factory.presenter.message;

import com.warner.factory.data.message.MessageDataSource;
import com.warner.factory.model.db.Message;
import com.warner.factory.presenter.BaseSourcePresenter;

import java.util.List;

/**
 * Created by warner on 2018/2/9.
 */

public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, MessageDataSource, Message, View>
        implements ChatContract.Presenter {

    private String mReceiverId;
    private int mReceiverType;

    public ChatPresenter(MessageDataSource messageDataSource, View view, String receiverId, int receiverType) {
        super(messageDataSource, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {

    }

    @Override
    public void pushText(String content) {

    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
        return false;
    }
}
