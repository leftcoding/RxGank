package com.left.gank.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
