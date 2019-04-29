package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.ui.base.DiffAdapter;
import com.left.gank.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends DiffAdapter<BindHolder> {
    private List<ItemModel> itemModels = new ArrayList<>();
    private List<ItemModel> newModels = new ArrayList<>();
    private List<Gank> ganks;

    private Context context;
    private ItemCallback itemCallBack;

    IosAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder bindHolder = null;
        switch (viewType) {
            case ViewType.NORMAL:
                bindHolder = new NormalHolder(parent, itemCallBack);
                break;
        }
        return bindHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BindHolder holder, int position) {
        holder.bindHolder(itemModels.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    void setItems(@NonNull List<Gank> list) {
        ganks = list;
    }

    public void clear() {
        itemModels.clear();
    }

    /**
     * 在主线程进行列表差异结果比较，不适合大型数据量
     */
    public void update() {
        final List<ItemModel> models = new ArrayList<>(itemModels);
        if (!ListUtils.isEmpty(ganks)) {
            for (Gank gank : ganks) {
                models.add(new TextViewModel(gank));
            }
        }
        updateAdapter(itemModels, models);
        itemModels = models;
    }

    @Override
    public void destroy() {
        itemCallBack = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}
