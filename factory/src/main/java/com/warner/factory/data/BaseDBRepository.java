package com.warner.factory.data;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.warner.factory.data.helper.DBHelper;
import com.warner.factory.model.db.BaseDBModel;
import com.warner.utils.CollectionUtil;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础的数据仓库
 *
 * Created by warner on 2018/2/6.
 */

public abstract class BaseDBRepository<Data extends BaseDBModel<Data>> implements DBDataSource<Data>,
        DBHelper.ChangedListener<Data>, QueryTransaction.QueryResultListCallback<Data> {

    private SuccessCallback<List<Data>> callback;

    private List<Data> mDataList = new LinkedList<>();

    private Class<Data> mDataClass;

    public BaseDBRepository() {
        // 获取当前类的泛型数组信息
        Type[] types = Reflector.getActualTypeArguments(BaseDBRepository.class, this.getClass());
        mDataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.callback = callback;
        registerDBChangeListener();
    }

    @Override
    public void dispose() {
        this.callback = null;
        unregisterDBChangeListener();
        mDataList.clear();
    }

    // 数据库统一通知的地方
    @Override
    public void onDataSave(Data[] list) {
        // 数据库数据变更的操作
        boolean isChaged = false;
        for (Data data : list) {
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChaged = true;
            }
        }

        if (isChaged) {
            notifyDataChage();
        }
    }

    // 数据库统一通知的地方
    @Override
    public void onDataDelete(Data[] list) {
        // 数据库数据删除的操作
        boolean isChanged = false;
        for (Data data : list) {
            if (mDataList.remove(data)) {
                isChanged = true;
            }
        }

        if (isChanged) {
            notifyDataChage();
        }
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0) {
            mDataList.clear();
            notifyDataChage();
            return;
        }

        Data[] data = CollectionUtil.toArray(mDataList, mDataClass);
        onDataSave(data);
    }


    /**
     * 检查一个data是否是我需要关注的数据
     * @param data
     * @return
     */
    protected abstract boolean isRequired(Data data);

    private void notifyDataChage() {
        SuccessCallback<List<Data>> callback = this.callback;
        if (callback != null) {
            callback.onDataLoaded(mDataList);
        }
    }

    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }

    }

    /**
     * 替换
     * @param index
     * @param data
     */
    private void replace(int index, Data data) {
        mDataList.remove(index);
        mDataList.add(index, data);
    }

    /**
     * 添加
     * @param data
     */
    private void insert(Data data) {
        mDataList.add(data);
    }

    private int indexOf(Data data) {
        int index = -1;
        for (Data data1 : mDataList) {
            index++;
            if (data1.isSame(data)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 添加数据库的监听
     */
    protected void registerDBChangeListener() {
        DBHelper.addChangedListener(mDataClass, this);
    }

    /**
     * 移除数据库监听
     */
    protected void unregisterDBChangeListener() {
        DBHelper.removeChangedListener(mDataClass, this);
    }
}
