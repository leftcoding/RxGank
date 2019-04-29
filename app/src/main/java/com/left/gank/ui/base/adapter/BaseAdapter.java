package com.left.gank.ui.base.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.left.gank.ui.base.item.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2018-09-25
 */

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final int DEFAULT_SPAN_COUNT = 1;
    private List<ViewModel> viewModels = new ArrayList<>();

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewModels.get(position).getViewType();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setViewManager(ViewManager viewManager) {
        viewManager.setAdapter(this);
    }

    void setViewModels(List<ViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public final GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            try {
                return viewModels.get(position).getSpanSize(DEFAULT_SPAN_COUNT, position);
            } catch (Exception e) {
                return DEFAULT_SPAN_COUNT;
            }
        }
    };
}
