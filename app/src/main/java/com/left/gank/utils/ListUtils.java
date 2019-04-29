package com.left.gank.utils;

import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class ListUtils {
    public static <E> int getSize(List<E> list) {
        return list == null ? 0 : list.size();
    }

    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static <E> boolean isNotEmpty(List<E> list) {
        return list != null && !list.isEmpty();
    }
}
