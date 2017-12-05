package com.warner.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by warner on 2017/12/5.
 *
 * 解决fragment的调度与重用问题
 * 达到最优的fragment切换
 */

public class NavHelper<T> {

    // 所有的Tab
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    private final int containerId;
    private final Context context;
    private final FragmentManager fragmentManager;
    private OnTabChangedListener<T> listener;
    // 当前选中的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加Tab
     * @param menuId 对应的菜单ID
     * @param tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前Tab
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的调度
     * @param menuId
     * @return
     */
    public boolean performClickMenu(int menuId) {
        // 去集合中寻找点击的菜单对应的Tab，有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }


    /**
     * 进行真实的tab选择操作
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;

        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //若当前的tab就是点击的tab，那么我们不做处理
                notifyTabReselect(tab);
                return;
            }
        }

        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 进行fragment的真实调度
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (oldTab != null) {
            if (oldTab.fragment != null) {
                // 从界面移除,但还在fragmentManager的缓存空间
                transaction.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                // 首次新建
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                // 缓存起来
                newTab.fragment = fragment;
                // 提交到FragmentManager
                transaction.add(containerId, fragment, newTab.clx.getName());
            } else {
                // 从fragmentManager的缓存空间中重新加载出来
                transaction.attach(newTab.fragment);
            }
        }
        // 提交事物
        transaction.commit();
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 通知tab选中
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChange(newTab, oldTab);
        }
    }

    /**
     * 二次点击tab的操作
     * @param tab
     */
    private void notifyTabReselect(Tab<T> tab) {

    }

    /**
     * 定义事件处理完成后的回调接口
     */
    public interface OnTabChangedListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }

    /**
     * 所有tab基础属性
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        // fragment对应的Class信息
        public Class<?> clx;
        // 额外字段用户自己设定需要使用
        public T extra;
        // 内部缓存对应的fragment
        Fragment fragment;
    }
}
