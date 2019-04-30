package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.diff.DiffCallback;
import com.left.gank.ui.base.DiffAdapter;
import com.left.gank.ui.ios.text.ItemCallback;
import com.left.gank.ui.ios.text.TextHolder;
import com.left.gank.ui.ios.text.TextModel;
import com.left.gank.ui.ios.text.ViewType;
import com.left.gank.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class IosAdapter1 extends DiffAdapter<BindHolder> {
    private List<ItemModel> itemModels = new ArrayList<>();

    private Context context;
    private ItemCallback itemCallBack;

    IosAdapter1(Context context) {
        this.context = context;
    }

    @Override
    public BindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder defaultHolder;
        switch (viewType) {
            case ViewType.NORMAL:
                defaultHolder = new TextHolder(parent, itemCallBack);
                break;
            default:
                defaultHolder = null;
                break;
        }
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BindHolder holder, int position) {
        final ItemModel viewItem = itemModels.get(position);
        switch (viewItem.getViewType()) {
            case ViewType.NORMAL:
                holder.bindHolder(viewItem);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return itemModels == null ? 0 : itemModels.size();
    }

    public void clearItems() {
        itemModels.clear();
    }

    /**
     * 在主线程进行列表差异结果比较，不适合大型数据量
     */
    public void update(List<Gank> list) {
        List<ItemModel> newModels = new ArrayList<>(itemModels);
        if (!ListUtils.isEmpty(list)) {
            for (Gank gank : list) {
                newModels.add(new TextModel(gank));
            }
        }

        final DiffCallback diffCallback = new DiffCallback<>(itemModels, newModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        itemModels.clear();
        itemModels.addAll(newModels);
        diffResult.dispatchUpdatesTo(this);

//        notifyDataSetChanged();
    }

    @Override
    public void destroy() {
        itemCallBack = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}

