package com.warner.factory.presenter.contact;

import com.warner.factory.model.db.User;
import com.warner.factory.presenter.BaseContract;

/**
 * Created by warner on 2018/1/25.
 */

public interface ContactContract {

    interface Presenter extends BaseContract.Presenter {

    }


    interface View extends BaseContract.RecyclerView<Presenter, User> {


    }
}
