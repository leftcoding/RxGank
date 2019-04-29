package com.left.gank.butterknife.adapter.more;

import com.left.gank.butterknife.adapter.ItemType;
import com.left.gank.butterknife.item.ItemModel;

public class LoadingItem extends ItemModel {
    @Override
    public int getViewType() {
        return ItemType.LOADING;
    }
}
