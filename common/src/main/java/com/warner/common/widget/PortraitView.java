package com.warner.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.warner.common.R;
import com.warner.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by warner on 2017/12/1.
 */

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUp(RequestManager manager, Author author) {
        if (author != null) {
            setUp(manager, author.getPortrait());
        }
    }

    public void setUp(RequestManager manager, String url) {
        setUp(manager, R.drawable.default_portrait, url);
    }

    public void setUp(RequestManager requestManager, int resourceId, String url) {
        if (TextUtils.isEmpty(url))
            url = "";
        requestManager.load(url).placeholder(resourceId).centerCrop().dontAnimate().into(this);
    }
}
