package com.warner.italker;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.warner.common.app.app.Activity;
import com.warner.common.app.widget.PortraitView;
import com.warner.italker.helper.NavHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author warner
 */
public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener {

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

	private NavHelper mHelper;

	@Override
	protected int getcontentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidget() {
		super.initWidget();

		mHelper = new NavHelper<>();
		mNavigation.setOnNavigationItemSelectedListener(this);
		Glide.with(this).load(R.mipmap.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
			@Override
			public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
				this.view.setBackground(resource.getCurrent());
			}
		});
	}

	@OnClick({R.id.im_search, R.id.btn_action})
	void onViewClick(View view) {
		if (view.getId() == R.id.im_search) {

		} else if (view.getId() == R.id.btn_action) {

		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		mTitle.setText(item.getTitle());
		return true;
	}
}
