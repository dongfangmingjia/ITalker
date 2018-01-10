package com.warner.common.widget.recycler;

/**
 * Created by warner on 2017/10/23.
 */

public interface AdapterCallBack<Data> {
	void updata(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
