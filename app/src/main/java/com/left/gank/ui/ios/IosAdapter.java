package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.ui.ios.text.ItemCallback;
import com.left.gank.ui.ios.text.TextHolder;
import com.left.gank.ui.ios.text.TextModel;
import com.left.gank.ui.ios.text.ViewType;
import com.left.gank.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends FootAdapter<BindHolder, List<Gank>> {
    private List<ItemModel> itemModels = new ArrayList<>();

    private ItemCallback itemCallback;

    IosAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<ItemModel> addItems() {
        return itemModels;
    }

    @Override
    public void fillItems(List<Gank> list) {
        itemModels.clear();
        appendItems(list);
    }

    @Override
    public void appendItems(List<Gank> list) {
        if (ListUtils.isNotEmpty(list)) {
            for (Gank gank : list) {
                if (gank == null) continue;
                itemModels.add(new TextModel(gank));
            }
        }
    }

    @Override
    protected BindHolder rxCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder bindHolder = null;
        switch (viewType) {
            case ViewType.NORMAL:
                bindHolder = new TextHolder(parent, itemCallback);
                break;
        }
        return bindHolder;
    }

    @Override
    public void destroy() {
        clear();
        itemCallback = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallback = itemCallBack;
    }
}
