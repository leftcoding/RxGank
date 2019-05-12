package com.left.gank.butterknife.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.left.gank.butterknife.item.ItemModel;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BasicHolder<II extends ItemModel> extends RecyclerView.ViewHolder {
    public BasicHolder(ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public abstract void bindHolder(II item);
}