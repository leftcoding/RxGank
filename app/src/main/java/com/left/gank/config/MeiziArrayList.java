package com.left.gank.config;


import android.business.domain.Solid;

import com.left.gank.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class MeiziArrayList {
    private static MeiziArrayList sMeiziArrayList;
    private List<Solid> mOneItemsList;
    private List<Solid> mMeiziList;
    private int mPage = 0;

    private MeiziArrayList() {
        mOneItemsList = new ArrayList<>();
        mMeiziList = new ArrayList<>();
    }

    public static MeiziArrayList getInstance() {
        if (sMeiziArrayList == null) {
            sMeiziArrayList = new MeiziArrayList();
        }
        return sMeiziArrayList;
    }

    public void refillOneItems(List<Solid> list) {
        if (ListUtils.isEmpty(mOneItemsList)) {
            mOneItemsList.addAll(list);
        }
    }

    public void addImages(List<Solid> list, int page) {
        if (mPage < page) {
            mMeiziList.addAll(list);
            mPage = page;
        }
    }

    public List<Solid> getImagesList() {
        return mMeiziList;
    }

    public List<Solid> getOneItemsList() {
        return mOneItemsList;
    }

    public boolean isOneItemsEmpty() {
        return ListUtils.getSize(mOneItemsList) <= 0;
    }

    public int getPage() {
        return mPage;
    }

    public Solid getResultBean(int position) {
        if (ListUtils.isEmpty(mMeiziList)) {
            return null;
        }
        return mMeiziList.get(position);
    }
}
