package com.warner.common.app.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warner.common.R;
import com.warner.common.app.widget.recycler.AdapterCallBack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by warner on 2017/10/23.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
		implements View.OnClickListener, View.OnLongClickListener, AdapterCallBack<Data> {

	private List<Data> mDataList;
	private AdapterListener<Data> mListener;


	/**
	 * 构造函数
	 */
	public RecyclerAdapter() {
		this(null);
	}

	public RecyclerAdapter(AdapterListener<Data> listener) {
		this(new ArrayList<Data>(), listener);
	}

	public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
		this.mDataList = dataList;
		this.mListener = listener;
	}

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
		// 设置View的Tag为ViewHolder， 进行双向绑定
		root.setTag(R.id.tag_recycler_holder, holder);

		// 进行界面注解绑定
		holder.mUnbinder = ButterKnife.bind(holder, root);
		// 绑定callBack
		holder.mCallBack = this;

		return holder;
	}


	/**
	 * 复写默认的布局类型返回
	 * @param position
	 * @return 返回的是XML的布局文件id
	 */
	@Override
	public int getItemViewType(int position) {
		return getItemViewType(position, mDataList.get(position));
	}

	/**
	 * 得到布局类型
	 * @param position
	 * @param data
	 * @return XML文件的id，用于创建ViewHolder
	 */
	@LayoutRes
	protected abstract int getItemViewType(int position, Data data);

	/**
	 * 创建一个ViewHolder
	 * @param root
	 * @param viewType 界面类型，约定为XML布局的id
	 * @return
	 */
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

	/**插入一条数据，并通知更新*/
	public void add(Data data) {
		mDataList.add(data);
		notifyItemInserted(mDataList.size() - 1);
	}

	/**
	 * 插入一堆数据，并通知这段集合更新
	 * @param datas
	 */
	public void add(Data... datas) {
		if (datas != null && datas.length > 0) {
			int startPos = mDataList.size();
			Collections.addAll(mDataList, datas);
			notifyItemRangeInserted(startPos, datas.length);
		}
	}


	/**
	 * 插入一堆数据，并通知这段集合更新
	 * @param dataList
	 */
	public void add(Collection<Data> dataList) {
		if (dataList != null && dataList.size() > 0) {
			int startPos = mDataList.size();
			mDataList.addAll(dataList);
			notifyItemRangeInserted(startPos, dataList.size());
		}
	}

	/**
	 * 清空所有
	 */
	public void clear() {
		mDataList.clear();
		notifyDataSetChanged();
	}

	/**
	 * 替换新的集合
	 * @param dataList
	 */
	public void replace(Collection<Data> dataList) {
		mDataList.clear();
		if (dataList == null || dataList.size() == 0) {
			return;
		}
		mDataList.addAll(dataList);
		notifyDataSetChanged();
	}


	@Override
	public void onClick(View v) {
		ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
		if (mListener != null) {
			int position = holder.getAdapterPosition();
			mListener.onItemClick(holder, mDataList.get(position));
		}
	}

	@Override
	public boolean onLongClick(View v) {
		ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
		if (mListener != null) {
			int position = holder.getAdapterPosition();
			mListener.onItemLongClick(holder, mDataList.get(position));
			return true;
		}
		return false;
	}

	/**
	 * 设置适配器监听
	 * @param listener
	 */
	public void setAdapterListener(AdapterListener<Data> listener) {
		this.mListener = listener;
	}

	/**
	 * 点击事件的监听器
	 * @param <Data>
	 */
	public interface AdapterListener<Data> {
		/**item点击监听*/
		void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);
		/**item长按监听*/
		void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
	}

	/**
	 * 自定义的ViewHolder
	 * @param <Data>
	 */
	public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
		private Unbinder mUnbinder;
		private AdapterCallBack<Data> mCallBack;
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

		/**
		 * Holder对自己对应的data的更新
		 * @param data
		 */
		public void updateData(Data data) {
			if (this.mCallBack != null) {
				mCallBack.updata(mData, this);
			}
		}
	}
}
