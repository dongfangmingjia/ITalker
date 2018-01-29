package com.warner.italker.fragment.search;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.warner.common.app.PresenterFragment;
import com.warner.common.widget.EmptyView;
import com.warner.common.widget.PortraitView;
import com.warner.common.widget.recycler.RecyclerAdapter;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.presenter.contact.FollowContract;
import com.warner.factory.presenter.contact.FollowPresenter;
import com.warner.factory.presenter.search.SearchContract;
import com.warner.factory.presenter.search.SearchUserPresenter;
import com.warner.italker.R;
import com.warner.italker.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchListener, SearchContract.UserView {


    @BindView(R.id.reycler)
    RecyclerView mReycler;
    @BindView(R.id.empty)
    EmptyView mEmpty;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mReycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mReycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // Cell的布局
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });

        mEmpty.bind(mReycler);
        setPlaceHolderView(mEmpty);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchUserPresenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(userCards.size() > 0);
    }

    /**
     * 每个Cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setUp(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(userCard.isFollow());
        }

        @OnClick(R.id.im_follow)
        void followClick() {
            mPresenter.follow(mData.getId());
        }

        @Override
        public void showError(int str) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化圆形的动画drawable
            LoadingCircleDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = {UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void followSuccess(UserCard userCard) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable)mFollow.getDrawable()).stop();
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }

            updateData(userCard);
        }
    }
}
