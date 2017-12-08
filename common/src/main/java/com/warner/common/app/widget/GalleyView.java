package com.warner.common.app.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.warner.common.R;
import com.warner.common.app.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IAMGE_COUNT = 3;// 最大的图片大小
    private static final int MIN_IMAGE_FILE_SIZE = 10* 1024; // 最小的图片大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // Cell点击操作，若点击是允许的，更新对应的Cell的状态，然后更新界面
                // 若不允许点击（已经达到最大选中数量）那么不刷新界面
                if (onItemSelectClick(image)) {
                    holder.updateData(image);
                }
            }
        });
    }


    /**
     * 点击的具体逻辑
     * @param image
     * @return true，需要刷新
     */
    private boolean onItemSelectClick(Image image) {
        // 是否进行刷新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            // 判断选中的Image中是否有点击的这个Image
            image.isSelected = false;
            mSelectedImages.remove(image);
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() >= MAX_IAMGE_COUNT) {
                // Toast提示选中已达上限
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IAMGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelected = true;
                notifyRefresh = true;
            }
        }

        // 若数据有改变，通知外面的监听者我们的数据选中改变了
        if (notifyRefresh) {
            notifySelectChanged();
        }
        return true;
    }

    /**
     * 初始化方法
     * @param loaderManager
     * @return 返回loader_id，可用于销毁loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * 得到选中的图片的全部地址
     * @return
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 清空选中的图片
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            image.isSelected = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        if (mListener != null) {
            mListener.onSelectedCountChanged(mSelectedImages.size());
        }
    }


    /**
     * 通知adapter数据更新
     * @param images
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 内部的数据结构
     */
    private static class Image {
        int id;// 数据ID
        String path; // 图片路径
        long date; // 图片创建时间
        boolean isSelected; // 是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }
    }


    /**
     *  Cell 对应的holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_selected);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)// 不使用缓存，直接从原图加载
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);

            mShade.setVisibility(image.isSelected ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelected);
        }
    }


    /**
     * 用于实际的数据加载的loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,// id
                MediaStore.Images.Media.DATA,// 图片路径
                MediaStore.Images.Media.DATE_ADDED// 图片创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 创建一个loader
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");// 倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 当loader加载完成时
            List<Image> images = new ArrayList<>();
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    // 移动游标到开始
                    data.moveToFirst();

                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        // 循环读取
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                            // 若文件不存在或者文件大小太小，则跳过
                            continue;
                        }

                        // 插入一条数据到列表
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);

                    } while (data.moveToNext());
                }
            }

            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // 当loader销毁或重置了, 进行界面清空
            updateSource(null);
        }
    }


    public interface SelectedChangeListener{
        void onSelectedCountChanged(int count);
    }
}
