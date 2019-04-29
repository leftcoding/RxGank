package com.left.gank.ui.android;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.left.gank.R;
import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class AndroidAdapter extends FootAdapter<BindHolder, List<Gank>> {
    private List<ItemModel> itemModels = new ArrayList<>();
    private Callback callback;

    public AndroidAdapter(Context context) {
        super(context);
    }

    @Override
    protected BindHolder rxCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindHolder bindHolder = null;
        switch (viewType) {
            case Type.TEXT:
                bindHolder = new TextHolder(parent, callback);
                break;
        }
        return bindHolder;
    }

    @Override
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
        for (Gank gank : list) {
            if (gank == null) continue;
            itemModels.add(new TextItem(gank));
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private static class TextItem extends ItemModel {
        private Gank gank;

        TextItem(Gank gank) {
            this.gank = gank;
        }

        public String getTime() {
            Date date = DateUtils.formatToDate(gank.publishedAt);
            return DateUtils.formatString(date, DateUtils.MM_DD);
        }

        @Override
        public int getViewType() {
            return Type.TEXT;
        }
    }

    static class TextHolder extends BindHolder<TextItem> {

        @BindView(R.id.author_name)
        TextView authorName;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.title)
        TextView title;

        private final Callback callback;

        TextHolder(ViewGroup parent, final Callback callback) {
            super(parent, R.layout.adapter_android);
            this.callback = callback;
        }

        @Override
        public void bindHolder(TextItem item) {
            final Gank gank = item.gank;
            time.setText(item.getTime());
            title.setText(gank.desc);
            authorName.setText(gank.who);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(v, gank);
                    }
                }
            });
        }
    }

    public interface Callback {
        void onItemClick(View view, Gank gank);
    }

    public interface Type {
        int TEXT = 0;
    }
}
