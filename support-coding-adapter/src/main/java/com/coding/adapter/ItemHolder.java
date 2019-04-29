package com.coding.adapter;

import android.view.View;

import androidx.annotation.NonNull;

public abstract class ItemHolder<E extends ViewModel> extends Holder {

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void onCreateHolder();

    public abstract void onBindItem();
}
