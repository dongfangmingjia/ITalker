package com.warner.factory.presenter.contact;

import com.warner.factory.model.card.UserCard;
import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/25.
 */

public interface FollowContract {

    interface Presenter extends BaseContract.Presenter {
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        void followSuccess(UserCard userCard);
    }
}
