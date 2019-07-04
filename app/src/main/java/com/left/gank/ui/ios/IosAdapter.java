package com.left.gank.ui.ios;

import android.business.domain.Gank;
import android.content.Context;
import android.view.ViewGroup;

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

import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2016-04-25
 */
class IosAdapter extends FootAdapter<BindHolder, List<Gank>> {
    private List<ItemModel> itemModels = new ArrayList<>();

    private ItemCallback itemCallback;

    IosAdapter(Context context) {
        super(context);
    }

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
        List<ItemModel> news = new ArrayList<>();
        if (ListUtils.isNotEmpty(list)) {
            for (Gank gank : list) {
                if (gank == null) continue;
                news.add(new TextModel(gank));
            }
        }
        itemModels.addAll(news);
    }

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
        itemCallback = null;
    }

    public void setOnItemClickListener(ItemCallback itemCallBack) {
        this.itemCallback = itemCallBack;
    }
}
