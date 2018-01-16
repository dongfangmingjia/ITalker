package com.warner.italker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.warner.common.app.BaseActivity;
import com.warner.factory.persistence.Account;
import com.warner.italker.activities.AccountActivity;
import com.warner.italker.activities.MainActivity;
import com.warner.italker.fragment.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends BaseActivity {

    private ColorDrawable mBgDrawable;

    @Override
    protected int getcontentLayoutId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        // 获取根布局
        View root = findViewById(R.id.activity_launch);
        // 获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        // 创建一个drawable
        ColorDrawable drawable = new ColorDrawable(color);
        // 设置给背景
        root.setBackground(drawable);

        mBgDrawable = drawable;
    }


    @Override
    protected void initData() {
        super.initData();
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverid();
            }
        });
    }


    private void waitPushReceiverid() {


        if (Account.isLogin()) {
            // 已经登录，判断是否已经绑定
            if (Account.isBind()) {
                skip();
                return;
            }
        } else {
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverid();
            }
        }, 500);
    }

    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                realSkip();
            }
        });
    }


    private void realSkip() {
        if (PermissionsFragment.hasAllPerm(this, getSupportFragmentManager())) {
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this );
            }
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void startAnim(float endProgress, final Runnable endCallback) {
        // 获取一个最终的颜色
        int finalColor = Resource.Color.WHITE;
        // 运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        // 构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, mProperty, evaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }


    private Property<LaunchActivity, Object> mProperty = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }


        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }
    };
}
