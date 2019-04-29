package com.coding.adapter;

/**
 * Create by LingYan on 2018-09-26
 */
public abstract class ViewModel implements ItemComparator {
    public abstract int getViewType();

    public abstract int getSpanSize(int size, int position);

    @Override
    public boolean isItemSame(ItemComparator itemComparator) {
        return false;
    }

    @Override
    public boolean isContentEqual(ItemComparator itemComparator) {
        return false;
    }
}
