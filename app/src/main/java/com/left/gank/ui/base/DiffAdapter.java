package com.left.gank.ui.base;

import android.content.Context;
import com.left.gank.butterknife.adapter.BaseAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.diff.DiffCallback;

import java.util.List;

/**
 * Create by LingYan on 2018-11-13
 */
public abstract class DiffAdapter<VH extends RecyclerView.ViewHolder> extends BaseAdapter<VH> {
    private RecyclerView.Adapter adapter;
    private List<ItemModel> curItemModels;

    public DiffAdapter(Context context) {
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adapter = recyclerView.getAdapter();
    }

    /**
     * 插入时更新数据源
     */
    protected void updateAdapter(List<ItemModel> oldModels, List<ItemModel> newModels) {
        DiffCallback diffCallback = new DiffCallback(oldModels, newModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//        更新数据有bug
//        diffResult.dispatchUpdatesTo(this);
        diffResult.dispatchUpdatesTo(listUpdateCallback);
        curItemModels = newModels;
        oldModels.clear();
        oldModels.addAll(newModels);
    }

    private final ListUpdateCallback listUpdateCallback = new ListUpdateCallback() {
        @Override
        public void onInserted(int position, int count) {
            // notifyItemRangeChanged 替换 notifyItemInserted
            adapter.notifyItemInserted(position);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            adapter.notifyItemRangeChanged(position, count, payload);
        }
    };

    @Override
    public long getItemId(int position) {
        return curItemModels.get(position).hashCode();
    }
}
