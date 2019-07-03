package com.left.gank.ui.ios.text;

import android.business.domain.Gank;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.gank.R;
import com.left.gank.butterknife.holder.BindHolder;

import butterknife.BindView;

public class TextHolder extends BindHolder<TextModel> {
    @BindView(R.id.author_name)
    TextView authorName;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.title)
    TextView title;

    private ItemCallback itemCallBack;

    public TextHolder(ViewGroup parent, ItemCallback callback) {
        super(parent, R.layout.adapter_ios);
        this.itemCallBack = callback;
    }

    @Override
    public void bindHolder(TextModel item) {
        final Gank gank = item.gank;

        time.setText(item.getTime());
        title.setText(gank.desc);
        authorName.setText(gank.who);

        itemView.setOnClickListener(v -> {
            if (itemCallBack != null) {
                itemCallBack.onItemClick(v, gank);
            }
        });
    }
}