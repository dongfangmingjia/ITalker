package com.warner.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.warner.common.app.BaseActivity;
import com.warner.italker.R;
import com.warner.italker.fragment.account.AccountTrigger;
import com.warner.italker.fragment.account.LoginFragment;
import com.warner.italker.fragment.account.RegisterFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements AccountTrigger {

    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.im_bg)
    ImageView mImBg;

    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_account;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        // 初始化背景
        Glide.with(this).load(R.drawable.bg_src_tianjin).centerCrop().into(new ViewTarget<ImageView, GlideDrawable>(mImBg) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Drawable drawable = resource.getCurrent();
                // 使用适配包进行包装
                drawable = DrawableCompat.wrap(drawable);
                drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);
                this.view.setImageDrawable(drawable);
            }
        });
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            fragment = mLoginFragment;
        }


        mCurFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }
}
