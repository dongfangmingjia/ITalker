package com.warner.factory.data.group;

import com.warner.factory.model.card.GroupCard;
import com.warner.factory.model.card.GroupMemberCard;

/**
 * Created by warner on 2018/2/2.
 */

public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);
    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
