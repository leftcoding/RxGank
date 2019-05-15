package com.left.gank.butterknife.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.left.gank.butterknife.adapter.more.EndHolder;
import com.left.gank.butterknife.adapter.more.EndItem;
import com.left.gank.butterknife.adapter.more.ErrorHolder;
import com.left.gank.butterknife.adapter.more.ErrorItem;
import com.left.gank.butterknife.adapter.more.LoadingHolder;
import com.left.gank.butterknife.adapter.more.LoadingItem;
import com.left.gank.butterknife.holder.BasicHolder;
import com.left.gank.butterknife.item.ItemModel;

import java.util.ArrayList;
import java.util.List;

public abstract class FootAdapter<VH extends BasicHolder, T> extends BaseAdapter<VH> {
    protected final Context context;
    private List<ItemModel> itemModels = new ArrayList<>();
    private boolean isFootModel = true;
    private boolean hasError;
    private boolean isEnd;
    private String endMsg;
    private String errMsg;
    private boolean isShowEnd;
    private ErrorListener errorListener;
    private int spanCount;

    public FootAdapter(Context context) {
        this.context = context;
        registerAdapterDataObserver(adapterDataObserver);
    }

    private final RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            clear();
            List<ItemModel> list = addItems();
            if (list != null) {
                itemModels.addAll(list);

                if (isFootModel) {
                    if (hasError) {
                        itemModels.add(new ErrorItem());
                        return;
                    }
                    if (isEnd) {
                        if (isShowEnd) {
                            itemModels.add(new EndItem());
                        }
                    } else {
                        itemModels.add(new LoadingItem());
                    }
                }
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    };

    private final ErrorListener localErrorListener = new ErrorListener() {
        @Override
        public void onClickError() {
            hasError = false;
            notifyDataSetChanged();
            if (errorListener != null) {
                errorListener.onClickError();
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getViewType();
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bindHolder(itemModels.get(position));
    }

    protected abstract VH rxCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BasicHolder vh;
        switch (viewType) {
            case ItemType.END:
                vh = new EndHolder(parent);
                break;
            case ItemType.LOADING:
                vh = new LoadingHolder(parent);
                break;
            case ItemType.ERROR:
                vh = new ErrorHolder(parent, localErrorListener);
                break;
            default:
                vh = rxCreateViewHolder(parent, viewType);
                break;
        }
        return (VH) vh;
    }

    protected abstract List<ItemModel> addItems();

    public abstract void fillItems(T list);

    public abstract void appendItems(T list);

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void clear() {
        itemModels.clear();
    }

    public void showError() {
        this.hasError = true;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        this.hasError = false;
    }

    protected void setFootModel(boolean isFootModel) {
        this.isFootModel = isFootModel;
    }

    public void setSpanCount(int count) {
        this.spanCount = count;
    }

    @Override
    public int getItemCount() {
        List<ItemModel> list = addItems();
        return isFootModel ? itemModels.size() : list == null ? 0 : list.size();
    }

    @Override
    public void destroy() {
        clear();
        unregisterAdapterDataObserver(adapterDataObserver);
    }

    public interface ErrorListener {
        void onClickError();
    }
}
