package com.warner.italker.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.warner.common.app.BaseApplication;
import com.warner.common.app.PresenterFragment;
import com.warner.common.widget.PortraitView;
import com.warner.factory.presenter.user.UpdateInfoContract;
import com.warner.factory.presenter.user.UpdateInfoPresenter;
import com.warner.italker.App;
import com.warner.italker.R;
import com.warner.italker.activities.MainActivity;
import com.warner.italker.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.im_sex)
    ImageView mSex;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    // 头像本地路径
    private String mPortraitPath;
    // 是否是男人
    private boolean mIsMan = true;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            App.showToast(R.string.data_rsp_error_unknown);
        }
    }


    private void loadPortrait(final Uri uri) {
        mPortraitPath = uri.getPath();
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);
    }

    @Override
    public void updateSuccessed() {
        MainActivity.show(getActivity());
        getActivity().finish();
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @OnClick({R.id.im_portrait, R.id.btn_submit, R.id.im_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_portrait:
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
                                .withAspectRatio(1, 1) // 设置裁剪宽高
                                .withMaxResultSize(520, 520) // 设置最大输出尺寸
                                .withOptions(options) // 设置相关参数
                                .start(getActivity()); // 启动

                    }
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
                break;
            case R.id.btn_submit:
                String desc = mDesc.getText().toString();
                mPresenter.update(mPortraitPath, desc, mIsMan);
                break;
            case R.id.im_sex:
                mIsMan = !mIsMan;

                Drawable drawable = getResources().getDrawable(mIsMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
                mSex.setImageDrawable(drawable);
                // 设置背景
                mSex.getDrawable().setLevel(mIsMan ? 0 : 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 停止loading
        mLoading.stop();
        mDesc.setEnabled(true);
        mPortrait.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);
    }


    @Override
    public void showLoading() {
        super.showLoading();

        // 停止loading
        mLoading.start();
        mDesc.setEnabled(false);
        mPortrait.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }
}
