package com.warner.italker.fragment.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.warner.common.app.BaseApplication;
import com.warner.common.app.BaseFragment;
import com.warner.common.widget.PortraitView;
import com.warner.italker.R;
import com.warner.italker.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UpdateInfoFragment extends BaseFragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }


    @OnClick(R.id.im_portrait)
    public void onViewClicked() {
        new GalleryFragment().setSelectedListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                // 设置图片处理的格式为JPEG
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置压缩后的图片精度
                options.setCompressionQuality(96);


                File dPath = BaseApplication.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1,1) // 设置裁剪宽高
                        .withMaxResultSize(520,520) // 设置最大输出尺寸
                        .withOptions(options) // 设置相关参数
                        .start(getActivity()); // 启动

            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {

        }
    }


    private void loadPortrait(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);
    }
}
