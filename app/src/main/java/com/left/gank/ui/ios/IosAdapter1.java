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
    private List<ItemModel> oldData = new ArrayList<>();
    private List<ItemModel> data = new ArrayList<>();

    private Context context;
    private ItemCallback itemCallBack;

    IosAdapter1(Context context) {
        this.context = context;
    }

    public void addData(List<Gank> list) {
        if (list != null) {
            List<ItemModel> temp = new ArrayList<>(data);
            temp.addAll(convert(list));
            setData(temp);
        }
    }

    private void setData(List<ItemModel> outData) {
        data.clear();
        data.addAll(outData);
        final DiffCallback diffCallback = new DiffCallback<>(oldData, outData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        oldData = new ArrayList<>();
        oldData.addAll(ListUtils.deepCopy(data));
        diffResult.dispatchUpdatesTo(this);


    }

    private List<ItemModel> convert(List<Gank> list) {
        List<ItemModel> models = new ArrayList<>();
        for (Gank gank : list) {
            if (gank == null) continue;
            models.add(new TextModel(gank));
        }
        return models;
    }

    @NonNull
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
        final ItemModel viewItem = oldData.get(position);
        switch (viewItem.getViewType()) {
            case ViewType.NORMAL:
                holder.bindHolder(viewItem);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return oldData.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void clearItems() {
        oldData.clear();
    }

    /**
     * 在主线程进行列表差异结果比较，不适合大型数据量
     */
    public void update(List<Gank> list) {
        List<ItemModel> newModels = new ArrayList<>(oldData);
        if (!ListUtils.isEmpty(list)) {
            for (Gank gank : list) {
                newModels.add(new TextModel(gank));
            }
        }

        final DiffCallback diffCallback = new DiffCallback<>(oldData, newModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        oldData.clear();
        oldData.addAll(newModels);
        diffResult.dispatchUpdatesTo(this);

//        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroy() {
        itemCallBack = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}

