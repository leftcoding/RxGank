package com.coding.adapter;

/**
 * Create by LingYan on 2018-09-26
 */
public interface ItemComparator {
    boolean isItemSame(ItemComparator itemComparator);

    boolean isContentEqual(ItemComparator itemComparator);
}
