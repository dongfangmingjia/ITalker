package com.warner.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by warner on 2018/1/10.
 */

public class CollectionUtil {


    /**
     * List集合转换为数组
     * @param items
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(List<T> items, Class<T> tClass) {
        if (items == null || items.size() == 0) {
            return null;
        }
        int size = items.size();
        try {
            T[] array = (T[]) Array.newInstance(tClass, size);
            return items.toArray(array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set 转换为数组
     * @param items
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(Set<T> items, Class<T> tClass) {
        if (items == null || items.size() == 0) {
            return null;
        }

        int size = items.size();
        try {
            T[] array = (T[]) Array.newInstance(tClass, size);
            return items.toArray(array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数组集合转换为HashSet集合
     * @param items
     * @param <T>
     * @return
     */
    public static <T> HashSet<T> toHashSet(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }

        HashSet<T> set = new HashSet<>();
        Collections.addAll(set, items);
        return set;
    }

    /**
     * 数组集合转换为ArrayList集合
     * @param items
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> toArrayList(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }

        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }
}