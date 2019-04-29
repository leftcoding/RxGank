package com.left.gank.butterknife.diff;

/**
 * Create by LingYan on 2018-09-26
 */
public interface ItemComparator {
    boolean areItemsTheSame(ItemComparator itemComparator);

    boolean areContentsTheSame(ItemComparator itemComparator);
}
