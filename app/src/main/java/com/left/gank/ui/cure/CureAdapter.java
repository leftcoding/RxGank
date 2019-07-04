package com.left.gank.ui.cure;

import android.business.domain.Gift;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.gank.R;
import com.left.gank.butterknife.adapter.BaseAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-07-05
 */
public class CureAdapter extends BaseAdapter<CureAdapter.NormalHolder> {
    private Callback callback;
    private List<Gift> gifts = new ArrayList<>();
    private final List<CureItem> items = new ArrayList<>();

    CureAdapter() {
    }

    @NonNull
    @Override
    public NormalHolder onCreateViewHolder(@NonNull ViewGroup parent, @ViewType.CureViewType int viewType) {
        NormalHolder viewHolder;
        if (viewType == ViewType.VIEW_TYPE_CURE) {
            viewHolder = new CureHolder(parent, callback);
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NormalHolder holder, int position) {
        holder.bindHolder(items.get(position));
    }

    public void setOnItemClickListener(Callback callback) {
        this.callback = callback;
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
        gifts.addAll(dailyGirlList);
        changeItems();
    }

    private void changeItems() {
        for (Gift gift : gifts) {
            if (gift == null) continue;
            items.add(new CureItem(gift));
        }
    }

    @Override
    public void destroy() {
        items.clear();
        gifts.clear();
    }

    static class CureHolder extends NormalHolder<CureItem> {
        @BindView(R.id.title)
        TextView title;

        private final Callback callback;

        CureHolder(ViewGroup parent, Callback callback) {
            super(parent, R.layout.adapter_daily_girl);
            this.callback = callback;
        }

        @Override
        public void bindHolder(CureItem item) {
            super.bindHolder(item);
            final Gift gift = item.gift;
            title.setText(gift.title);

            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onClick(gift.url);
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
            return ViewType.VIEW_TYPE_CURE;
        }
    }

    interface Callback {
        void onClick(String url);
    }

    abstract static class NormalHolder<TT extends ItemModel> extends BindHolder<TT> {

        NormalHolder(ViewGroup parent, int layoutRes) {
            super(parent, layoutRes);
        }

        @Override
        public void bindHolder(TT item) {

        }
    }

    public interface ViewType {
        int VIEW_TYPE_CURE = 1;

        @IntDef(VIEW_TYPE_CURE)
        @Retention(RetentionPolicy.SOURCE)
        @interface CureViewType {
        }
    }
}
