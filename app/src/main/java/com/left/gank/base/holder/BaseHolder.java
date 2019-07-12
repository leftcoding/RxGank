package com.left.gank.base.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by LingYan on 2016-11-10
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder {
    public boolean isShowing;

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract View getView();
}
