package com.warner.italker.fragment;

import com.warner.common.app.app.Fragment;
import com.warner.common.app.widget.GalleyView;
import com.warner.italker.R;

import butterknife.BindView;

/**
 * Created by warner on 2017/12/8.
 */

public class ActiveFragment extends Fragment {

    @BindView(R.id.galleyView)
    GalleyView mGalleyView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalleyView.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
