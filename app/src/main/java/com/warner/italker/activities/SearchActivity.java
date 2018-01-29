package com.warner.italker.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.warner.common.app.BaseFragment;
import com.warner.common.app.ToolbarActivity;
import com.warner.italker.R;
import com.warner.italker.fragment.search.SearchGroupFragment;
import com.warner.italker.fragment.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "extra_type";
    public static final int TYPE_USER = 1; // 搜索人
    public static final int TYPE_GROUP = 2; // 搜索群
    private int mType;

    private SearchListener mSearchListener;

    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        mType = bundle.getInt(EXTRA_TYPE);
        return mType == TYPE_USER || mType == TYPE_GROUP;
    }

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        BaseFragment fragment;
        if (mType == TYPE_USER) {
            SearchUserFragment userFragment = new SearchUserFragment();
            fragment = userFragment;
            mSearchListener = userFragment;
        } else {
            SearchGroupFragment groupFragment = new SearchGroupFragment();
            fragment = groupFragment;
            mSearchListener = groupFragment;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // 找到搜索菜单
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // 当点击了提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索发起点
     *
     * @param query
     */
    private void search(String query) {
        if (mSearchListener != null) {
            mSearchListener.search(query);
        }
    }

    /**
     * 搜索接口
     */
    public interface SearchListener {

        void search(String content);
    }
}
