package com.warner.italker.fragment.message;

import android.os.Bundle;

import com.warner.common.app.BaseFragment;
import com.warner.italker.activities.MessageActivity;

/**
 * Created by warner on 2018/2/6.
 */

public abstract class ChatFragment extends BaseFragment {

    protected String mReceiverId;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }
}
