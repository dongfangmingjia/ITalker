package com.warner.factory.presenter.contact;

import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by warner on 2018/1/25.
 */

public class FollowPresenter extends BasePresenter<FollowContract.View> implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();

        UserHelper.followUser(id, this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.followSuccess(userCard);
                }
            });
        }
    }

    @Override
    public void onDatanotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
