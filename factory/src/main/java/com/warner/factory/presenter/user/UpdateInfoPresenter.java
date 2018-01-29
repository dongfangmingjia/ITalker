package com.warner.factory.presenter.user;

import android.text.TextUtils;

import com.warner.factory.Factory;
import com.warner.factory.R;
import com.warner.factory.data.DataSource;
import com.warner.factory.data.helper.UserHelper;
import com.warner.factory.model.api.user.UserUpdateModel;
import com.warner.factory.model.card.UserCard;
import com.warner.factory.model.db.User;
import com.warner.factory.net.UploadHelper;
import com.warner.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by warner on 2018/1/17.
 */

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View> implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {

    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();

        final UpdateInfoContract.View view = getView();


        if (!TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            // 上传头像
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        view.showError(R.string.data_upload_error);
                    } else {
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSuccessed();
            }
        });
    }

    @Override
    public void onDatanotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();

        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
