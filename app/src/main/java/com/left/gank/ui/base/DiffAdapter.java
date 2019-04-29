package com.left.gank.ui.base;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.left.gank.butterknife.adapter.BaseAdapter;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.diff.DiffCallback;

import java.util.List;

/**
 * Create by LingYan on 2018-11-13
 */
public abstract class DiffAdapter<VH extends RecyclerView.ViewHolder> extends BaseAdapter<VH> {

    public DiffAdapter() {
    }

    /**
     * 插入时更新数据源
     */
    protected void updateAdapter(List<ItemModel> oldModels, List<ItemModel> newModels) {
        final DiffCallback diffCallback = new DiffCallback(oldModels, newModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
    }
}
