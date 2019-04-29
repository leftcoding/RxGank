package com.left.gank.butterknife.diff;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Create by LingYan on 2018-05-30
 */

public class DiffUtil extends androidx.recyclerview.widget.DiffUtil.Callback {
    private List<ItemComparator> oldList;
    private List<ItemComparator> newList;

    public DiffUtil(@NonNull List<ItemComparator> oldList, @NonNull List<ItemComparator> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        final ItemComparator oldItem = oldList.get(oldItemPosition);
        final ItemComparator newItem = newList.get(newItemPosition);
        return oldItem.areItemsTheSame(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ItemComparator oldItem = oldList.get(oldItemPosition);
        final ItemComparator newItem = newList.get(newItemPosition);
        return oldItem.areContentsTheSame(newItem);
    }
}
