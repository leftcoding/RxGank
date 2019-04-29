package com.left.gank.butterknife.item;

import com.left.gank.butterknife.diff.ItemComparator;

/**
 * Create by LingYan on 2018-11-12
 */
public abstract class ItemModel implements ItemComparator {
    public abstract int getViewType();

    public int getSpanSize(int size, int position) {
        return size;
    }

    @Override
    public boolean areItemsTheSame(ItemComparator itemComparator) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(ItemComparator itemComparator) {
        return false;
    }
}
