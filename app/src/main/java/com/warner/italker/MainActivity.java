package com.warner.italker;

import android.widget.TextView;

import com.warner.common.app.app.Activity;

import butterknife.BindView;

/**
 * @author warner
 */
public class MainActivity extends Activity {

	@BindView(R.id.tv_text)
	TextView tv_text;

	@Override
	protected int getcontentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		tv_text.setText("Hello 你好");
	}
}
