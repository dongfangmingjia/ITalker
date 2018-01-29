package com.warner.factory.presenter.search;

import com.warner.factory.presenter.BasePresenter;

/**
 * Created by warner on 2018/1/25.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter {

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
