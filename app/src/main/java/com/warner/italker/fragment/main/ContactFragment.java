package com.warner.italker.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.warner.common.app.PresenterFragment;
import com.warner.common.widget.EmptyView;
import com.warner.common.widget.PortraitView;
import com.warner.common.widget.recycler.RecyclerAdapter;
import com.warner.factory.model.db.User;
import com.warner.factory.presenter.contact.ContactContract;
import com.warner.factory.presenter.contact.ContactPresenter;
import com.warner.italker.R;
import com.warner.italker.activities.MessageActivity;

import butterknife.BindView;

/**
 * Created by warner on 2017/12/8.
 */

public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View {
    @BindView(R.id.reycler)
    RecyclerView mReycler;
    @BindView(R.id.empty)
    EmptyView mEmpty;

    private RecyclerAdapter<User> mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mEmpty.bind(mReycler);
        setPlaceHolderView(mEmpty);

        mReycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mReycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                // 跳转聊天界面
                MessageActivity.show(getContext(), user);
            }
        });
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 界面显示
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setUp(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }
    }
}
