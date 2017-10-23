package com.warner.common.app.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by warner on 2017/10/23.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener {

	private final List<Data> mDataList = new ArrayList<>();


	/**
	 * 创建ViewHolder
	 * @param parent RecyclerView
	 * @param viewType 界面类型，约定为xml布局id
	 * @return
	 */
	@Override
	public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
		// 得到LayoutInflater用于将xml初始化为View
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		// 将xml id为viewType的文件初始化为一个root view
		View root = inflater.inflate(viewType, parent, false);
		// 通过子类必须实现的方法，得到一个ViewHolder
		ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

		// 设置事件点击
		root.setOnClickListener(this);
		root.setOnLongClickListener(this);
		return null;
	}


	protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

	@Override
	public void onBindViewHolder(ViewHolder<Data> holder, int position) {
		// 获取需要绑定的数据
		Data data = mDataList.get(position);
		// 触发holder的绑定方法
		holder.bind(data);
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

		protected Data mData;

		public ViewHolder(View itemView) {
			super(itemView);
		}

		/**
		 * 用于绑定数据的触发
		 * @param data
		 */
		void bind(Data data) {
			this.mData = data;
		}

		/**触发绑定数据时的回调，必须复写*/
		protected abstract void onBind(Data data);

		public void updateData(Data data) {

		}
	}
}
