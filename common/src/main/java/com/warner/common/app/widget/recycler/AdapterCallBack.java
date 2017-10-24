package com.warner.common.app.widget.recycler;

import com.warner.common.app.widget.RecyclerAdapter;

/**
 * Created by warner on 2017/10/23.
 */

public interface AdapterCallBack<Data> {
	void updata(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
