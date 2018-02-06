package com.warner.factory.presenter;

import com.warner.factory.data.DBDataSource;
import com.warner.factory.data.DataSource;

import java.util.List;

/**
 * 基础仓库源的Presenter定义
 *
 * Created by warner on 2018/2/6.
 */

public abstract class BaseSourcePresenter<Data, Souece extends DBDataSource<Data>, ViewModel,
        View extends BaseContract.RecyclerView> extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SuccessCallback<List<Data>> {

    private Souece mSouece;

    public BaseSourcePresenter(Souece souece, View view) {
        super(view);
        this.mSouece = souece;
    }

    @Override
    public void start() {
        super.start();
        if (mSouece != null) {
            mSouece.load(this);
        }
    }


    @Override
    public void destory() {
        super.destory();
        mSouece.dispose();
        mSouece = null;
    }
}
