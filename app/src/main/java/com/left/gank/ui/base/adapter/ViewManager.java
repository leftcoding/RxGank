package com.left.gank.ui.base.adapter;

import com.left.gank.ui.base.item.ViewModel;

import java.util.List;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class ViewManager {
    public abstract void setAdapter(BaseAdapter baseAdapter);

    public abstract List<ViewModel> getModels();

    public abstract void notifyDataSetChanged();
}
