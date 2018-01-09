package com.warner.italker;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.warner.common.app.app.BaseActivity;
import com.warner.common.app.widget.PortraitView;
import com.warner.italker.activities.AccountActivity;
import com.warner.italker.fragment.main.ActiveFragment;
import com.warner.italker.fragment.main.ContactFragment;
import com.warner.italker.fragment.main.GroupFragment;
import com.warner.italker.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author warner
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {

	@BindView(R.id.appbar)
	View mLayAppbar;
	@BindView(R.id.txt_title)
	TextView mTitle;
	@BindView(R.id.im_portrait)
	PortraitView mPortrait;
	@BindView(R.id.lay_container)
	FrameLayout mLayContainer;
	@BindView(R.id.navigation)
	BottomNavigationView mNavigation;
	@BindView(R.id.btn_action)
	FloatActionButton mAction;

	private NavHelper<Integer> mHelper;

	@Override
	protected int getcontentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidget() {
		super.initWidget();

		initHelper();
		mNavigation.setOnNavigationItemSelectedListener(this);
		Glide.with(this).load(R.mipmap.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
			@Override
			public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
				this.view.setBackground(resource.getCurrent());
			}
		});
	}

	/**
	 * 初始化fragmenthelper及fragment
	 */
	private void initHelper() {
		mHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
		mHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
				.add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
				.add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
	}

	@Override
	protected void initData() {
		super.initData();
		// 默认第一个tab被选中
		Menu menu = mNavigation.getMenu();
		menu.performIdentifierAction(R.id.action_home, 0);
	}

	@OnClick({R.id.im_search, R.id.btn_action})
	void onViewClick(View view) {
		if (view.getId() == R.id.im_search) {

		} else if (view.getId() == R.id.btn_action) {
			AccountActivity.show(this);
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		return mHelper.performClickMenu(item.getItemId());
	}

	@Override
	public void onTabChange(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
		mTitle.setText(newTab.extra);

		// 浮动按钮的显示与隐藏
		float transY = 0;
		float rotation = 0;

		if (Objects.equals(newTab.extra, R.string.title_home)) {
			transY = Ui.dipToPx(getResources(), 76);
		} else {
			if (Objects.equals(newTab.extra, R.string.title_group)) {
				mAction.setImageResource(R.drawable.ic_group_add);
				rotation = -360;
			} else {
				mAction.setImageResource(R.drawable.ic_contact_add);
				rotation = 360;
			}
		}

		mAction.animate().rotation(rotation).translationY(transY)
				.setInterpolator(new AnticipateOvershootInterpolator(1))
				.setDuration(480).start();
	}
}
