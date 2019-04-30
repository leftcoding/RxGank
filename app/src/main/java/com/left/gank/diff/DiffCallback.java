package com.left.gank.diff;

import androidx.recyclerview.widget.DiffUtil;

import com.left.gank.butterknife.diff.ItemComparator;

import java.util.List;

/**
 * Create by LingYan on 2018-11-12
 */
public class DiffCallback<T extends ItemComparator> extends DiffUtil.Callback {
    private List<T> oldList;
    private List<T> newList;

    public DiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ItemComparator oldItem = oldList.get(oldItemPosition);
        ItemComparator newItem = newList.get(newItemPosition);
        return newItem.areItemsTheSame(oldItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ItemComparator oldItem = oldList.get(oldItemPosition);
        ItemComparator newItem = newList.get(newItemPosition);
        return newItem.areContentsTheSame(oldItem);
    }
}
