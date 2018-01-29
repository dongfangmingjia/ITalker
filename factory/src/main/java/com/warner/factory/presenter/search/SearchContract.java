package com.warner.factory.presenter.search;

import com.warner.factory.model.card.GroupCard;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by warner on 2018/1/25.
 */

public interface SearchContract {

    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }


    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }


    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }
}
