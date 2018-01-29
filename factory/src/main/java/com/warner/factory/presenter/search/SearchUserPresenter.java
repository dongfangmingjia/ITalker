package com.warner.factory.presenter.search;

import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * Created by warner on 2018/1/25.
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {

    private Call mSearchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        Call call = mSearchCall;
        if (call != null && !call.isCanceled()) {
            // 上一次请求没有取消，则调用取消请求的操作
            call.cancel();
        }

        mSearchCall = UserHelper.searchUser(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.onSearchDone(userCards);
                }
            });
        }

    }

    @Override
    public void onDatanotAvailable(final int strRes) {
        final SearchContract.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.showError(strRes);
                }
            });
        }
    }
}
