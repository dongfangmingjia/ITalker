package com.warner.common.app.widget.recycler;

/**
 * Created by warner on 2017/10/23.
 */

public interface AdapterCallBack<Data> {
	void updata(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
