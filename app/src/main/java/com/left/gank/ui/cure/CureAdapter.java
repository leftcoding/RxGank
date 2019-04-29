package com.left.gank.ui.cure;

import com.left.gank.butterknife.adapter.BaseAdapter;
import android.ly.business.domain.Gift;
import androidx.annotation.IntDef;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.gank.R;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.left.gank.ui.cure.CureAdapter.ViewType.VIEW_TYPE_CURE;

/**
 * Create by LingYan on 2016-07-05
 */
public class CureAdapter extends BaseAdapter<CureAdapter.NormalHolder> {
    private ItemCallback itemCallback;
    private List<Gift> gifts = new ArrayList<>();
    private final List<CureItem> items = new ArrayList<>();

    CureAdapter() {
        setHasStableIds(true);
    }

    @Override
    public NormalHolder onCreateViewHolder(ViewGroup parent, @ViewType.CureViewType int viewType) {
        NormalHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_CURE:
                viewHolder = new CureHolder(parent);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NormalHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_CURE:
                ((CureHolder) holder).bindHolder(items.get(position), itemCallback);
                break;
        }
    }

    public void setOnItemClickListener(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (!items.isEmpty()) {
            return items.get(position).getViewType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void refillItem(List<Gift> dailyGirlList) {
        gifts.clear();
        appendItem(dailyGirlList);
    }

    public void appendItem(List<Gift> dailyGirlList) {
        int startIndex = items.size();
        gifts.addAll(dailyGirlList);
        changeItems(startIndex);
    }

    private void changeItems(int startIndex) {
        for (int i = startIndex, size = gifts.size(); i < size; i++) {
            final Gift gift = gifts.get(i);
            if (gift != null) {
                items.add(new CureItem(gift));
            }
        }
    }

    @Override
    public void destroy() {
        items.clear();
        gifts.clear();
        itemCallback = null;
    }

    static class CureHolder extends NormalHolder<CureItem> {
        @BindView(R.id.title)
        TextView title;

        CureHolder(ViewGroup parent) {
            super(parent, R.layout.adapter_daily_girl);
        }

        @Override
        void bindHolder(CureItem item, ItemCallback callback) {
            super.bindHolder(item, callback);
            final Gift gift = item.gift;
            title.setText(gift.title);

            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClick(gift.url);
                }
            });
        }
    }

    static class CureItem extends ItemModel {
        final Gift gift;

        CureItem(Gift gift) {
            this.gift = gift;
        }

        @Override
        public int getViewType() {
            return VIEW_TYPE_CURE;
        }
    }

    interface ItemCallback {
        void onItemClick(String url);
    }

    abstract static class NormalHolder<TT extends ItemModel> extends BindHolder<TT> {

        NormalHolder(ViewGroup parent, int layoutRes) {
            super(parent, layoutRes);
        }

        void bindHolder(TT item, ItemCallback callback) {

        }

        @Override
        public void bindHolder(TT item) {

        }
    }

    interface ViewType {
        int VIEW_TYPE_CURE = 1;

        @IntDef(VIEW_TYPE_CURE)
        @Retention(RetentionPolicy.SOURCE)
        @interface CureViewType {
        }
    }
}
