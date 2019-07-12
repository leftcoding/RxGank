package com.left.gank.base.item;

import com.left.gank.butterknife.diff.ItemComparator;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class ViewModel implements ItemComparator {
    public abstract int getViewType();

    public abstract int getSpanSize(int size, int position);

    @Override
    public boolean areItemsTheSame(ItemComparator itemComparator) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(ItemComparator itemComparator) {
        return false;
    }
}
