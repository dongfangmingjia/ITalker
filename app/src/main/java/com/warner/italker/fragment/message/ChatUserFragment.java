package com.warner.italker.fragment.message;


import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.warner.common.widget.PortraitView;
import com.warner.factory.model.db.User;
import com.warner.factory.presenter.message.ChatContract;
import com.warner.italker.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {

    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;

    private MenuItem mUserInfoMenuItem;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mImPortrait;
        MenuItem menuItem = mUserInfoMenuItem;

        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            // abs 运算
            verticalOffset = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                // 关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);

                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {
                // 中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha((int) (255 - (255 - progress)));
            }
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });


        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }


    @OnClick(R.id.im_portrait)
    void onPortraitClick() {

    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(User user) {
        // 对你要发送消息的用户信息的初始化

    }
}
