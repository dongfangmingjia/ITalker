package com.warner.italker.fragment.search;


import android.support.v4.app.Fragment;

import com.warner.common.app.PresenterFragment;
import com.warner.factory.model.card.GroupCard;
import com.warner.factory.presenter.search.SearchContract;
import com.warner.factory.presenter.search.SearchGroupPresenter;
import com.warner.italker.R;
import com.warner.italker.activities.SearchActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchListener, SearchContract.GroupView {


    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }
}
