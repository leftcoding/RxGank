package com.left.gank.base;

import com.left.gank.butterknife.adapter.BaseAdapter;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.diff.DiffCallback;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by LingYan on 2018-11-13
 */
public abstract class DiffAdapter<VH extends RecyclerView.ViewHolder> extends BaseAdapter<VH> {
    private RecyclerView.Adapter adapter;

    public DiffAdapter() {
        this.adapter = this;
    }

    /**
     * 插入时更新数据源
     */
    protected void updateAdapter(List<ItemModel> oldModels, List<ItemModel> newModels) {
        final DiffCallback diffCallback = new DiffCallback<>(oldModels, newModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);

//        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
//            @Override
//            public void onInserted(int position, int count) {
//                adapter.notifyItemInserted(position);
//            }
//
//            @Override
//            public void onRemoved(int position, int count) {
//                adapter.notifyItemRangeRemoved(position, count);
//            }
//
//            @Override
//            public void onMoved(int fromPosition, int toPosition) {
//                adapter.notifyItemMoved(fromPosition, toPosition);
//            }
//
//            @Override
//            public void onChanged(int position, int count, @Nullable Object payload) {
//                adapter.notifyItemRangeChanged(position, count, payload);
//            }
//        });
    }
}
