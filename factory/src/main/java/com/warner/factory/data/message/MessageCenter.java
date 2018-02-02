package com.warner.factory.data.message;

import com.warner.factory.model.card.MessageCard;

/**
 * Created by warner on 2018/2/2.
 */

public interface MessageCenter {

    void dispatch(MessageCard... messageCards);
}
