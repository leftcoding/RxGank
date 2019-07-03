package com.left.gank.ui.pure;

import android.business.domain.Gift;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.left.gank.R;
import com.left.gank.butterknife.adapter.BaseAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-25
 */
public class PureAdapter extends BaseAdapter<BindHolder> {
    private final List<Gift> gifts = new ArrayList<>();
    private final List<ItemModel> items = new ArrayList<>();
    private Callback callback;

    PureAdapter() {
        registerAdapterDataObserver(observer);
    }

    private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            items.clear();
            if (ListUtils.isNotEmpty(gifts)) {
                for (Gift gift : gifts) {
                    if (gift == null) continue;
                    items.add(new GiftItem(gift));
                }
            }
        }
    };

    @NonNull
    @Override
    public BindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder holder;
        if (viewType == Type.IMAGE) {
            holder = new GiftHolder(parent, callback);
        } else {
            holder = null;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BindHolder holder, int position) {
        holder.bindHolder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!items.isEmpty()) {
            return items.get(position).getViewType();
        }
        return super.getItemViewType(position);
    }

    void refillItems(List<Gift> getResults) {
        gifts.clear();
        items.clear();
        appendItems(getResults);
    }

    public void appendItems(List<Gift> getResults) {
        gifts.addAll(getResults);
    }

    public void clear() {
        gifts.clear();
    }

    public void setOnItemClickListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void destroy() {
        items.clear();
        gifts.clear();
        unregisterAdapterDataObserver(observer);
    }

    static class GiftHolder extends BindHolder<GiftItem> {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.goods_background)
        ImageView goodsBackground;

        @BindView(R.id.author)
        TextView author;

        private final Callback callback;

        GiftHolder(ViewGroup parent, Callback callback) {
            super(parent, R.layout.adapter_gift);
            this.callback = callback;
        }

        @Override
        public void bindHolder(GiftItem item) {
            final Gift gift = item.gift;
            final Context context = itemView.getContext();
            title.setText(gift.title);
            author.setText(gift.time);

            final GlideUrl glideUrl = new GlideUrl(gift.imgUrl, new LazyHeaders.Builder()
                    .addHeader("referer", "http://www.mzitu.com/mm/")
                    .build());

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(goodsBackground);

            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClick(gift);
                }
            });
        }
    }

    static class GiftItem extends ItemModel {
        final Gift gift;

        GiftItem(Gift gift) {
            this.gift = gift;
        }

        @Override
        public int getViewType() {
            return Type.IMAGE;
        }
    }

    public interface Type {
        int IMAGE = 0;
    }

    interface Callback {
        void onItemClick(Gift gift);
    }
}
