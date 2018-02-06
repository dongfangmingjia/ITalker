package com.warner.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.warner.factory.utils.DiffUiDataCallback;

/**
 * Created by warner on 2018/2/6.
 */

public abstract class BaseDBModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {
}
