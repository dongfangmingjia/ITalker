package com.warner.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 *
 * Created by warner on 2018/2/6.
 */

public interface DBDataSource<Data> extends DataSource {
    /**
     * 数据源加载
     * @param callback
     */
    void load(SuccessCallback<List<Data>> callback);
}
