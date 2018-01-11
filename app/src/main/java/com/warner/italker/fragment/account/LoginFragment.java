package com.warner.italker.fragment.account;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warner.common.app.BaseFragment;
import com.warner.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAccountTrigger.triggerView();
    }
}
