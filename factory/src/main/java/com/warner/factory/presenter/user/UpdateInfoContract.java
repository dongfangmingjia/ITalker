package com.warner.factory.presenter.user;

import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/17.
 */

public interface UpdateInfoContract {

    interface Presenter extends BaseContract.Presenter {
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {

        void updateSuccessed();
    }
}
