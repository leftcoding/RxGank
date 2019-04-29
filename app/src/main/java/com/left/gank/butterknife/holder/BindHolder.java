package com.left.gank.butterknife.holder;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.left.gank.butterknife.item.ItemModel;

import butterknife.ButterKnife;

/**
 * Create by LingYan on 2017-10-13
 */

public abstract class BindHolder<II extends ItemModel> extends BasicHolder<II> {
    public BindHolder(ViewGroup parent, @LayoutRes int layout) {
        super(parent, layout);
        ButterKnife.bind(this, itemView);
    }
}
