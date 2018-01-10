package com.warner.italker.fragment.main;

import com.warner.common.app.BaseFragment;
import com.warner.common.widget.GalleryView;
import com.warner.italker.R;

import butterknife.BindView;

/**
 * Created by warner on 2017/12/8.
 */

public class ActiveFragment extends BaseFragment {

    @BindView(R.id.galleryView)
    GalleryView mGalleyView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalleyView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
