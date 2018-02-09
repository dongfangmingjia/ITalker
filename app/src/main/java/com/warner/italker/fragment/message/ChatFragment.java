package com.warner.italker.fragment.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.warner.common.app.PresenterFragment;
import com.warner.common.widget.PortraitView;
import com.warner.common.widget.adapter.TextWatcherAdapter;
import com.warner.common.widget.recycler.RecyclerAdapter;
import com.warner.factory.model.db.Message;
import com.warner.factory.model.db.User;
import com.warner.factory.persistence.Account;
import com.warner.factory.presenter.message.ChatContract;
import com.warner.italker.R;
import com.warner.italker.activities.MessageActivity;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by warner on 2018/2/6.
 */

public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter> implements
        AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel> {

    @BindView(R.id.im_header)
    ImageView mImHeader;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.reycler)
    RecyclerView mReycler;
    @BindView(R.id.btn_submit)
    ImageView mBtnSubmit;
    @BindView(R.id.edit_content)
    EditText mEditContent;

    protected String mReceiverId;
    private Adapter mAdapter;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        initToolbar();
        initAppBar();
        initEditContent();

        mReycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mReycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    private void initAppBar() {
        mAppbar.addOnOffsetChangedListener(this);
    }

    protected void initToolbar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }


    private void initEditContent() {
        mEditContent.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                // 设置状态改变对应的icon
                mBtnSubmit.setActivated(needSendMsg);
            }
        });
    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {

    }


    @OnClick(R.id.btn_record)
    void onRecordClick() {

    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mBtnSubmit.isActivated()) {
            // 发送
            String content = mEditContent.getText().toString();
            mEditContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {

    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_FILE:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
            }
            return 0;
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);

                default:
                    return new TextHolder(root);
            }
        }
    }

    // ViewHolder的基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        // 允许为空
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载
            sender.load();
            // 头像加载
            mPortrait.setUp(Glide.with(ChatFragment.this), sender);


            if (mLoading != null) {
                // 当前布局在右边
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    // 正常状态，隐藏loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                    mLoading.stop();
                }

                // 只有在发送失败的时候才能点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }

        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            // 重新发送
            if (mLoading != null) {
                // 必须是右边的才允许发送

            }
        }
    }

    // 文字的Holder
    class TextHolder extends BaseHolder {

        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }


        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            mContent.setText(message.getContent());
        }
    }

    // 语音的Holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }


        @Override
        protected void onBind(Message message) {
            super.onBind(message);
        }
    }

    // 图片的Holder
    class PicHolder extends BaseHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
        }
    }
}
