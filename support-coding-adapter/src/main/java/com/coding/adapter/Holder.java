package com.coding.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class Holder extends RecyclerView.ViewHolder {
    public Holder(@NonNull View itemView) {
        super(itemView);
    }
}
