package com.left.gank.ui.welfare;

import android.business.domain.Gank;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.left.gank.R;
import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2018-09-25
 */
public class WelfareAdapter extends FootAdapter<WelfareAdapter.ViewHolder, List<Gank>> {
    private static ArrayMap<String, Integer> heights = new ArrayMap<>();
    private List<ItemModel> models = new ArrayList<>();
    private ItemClickListener itemClickListener;

    WelfareAdapter(Context context) {
        super(context);
        setFootModel(false);
    }

    public void setListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected ViewHolder rxCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, itemClickListener, heights);
    }

    @Override
    protected List<ItemModel> addItems() {
        return models;
    }

    @Override
    public void fillItems(List<Gank> list) {
        models.clear();
        appendItems(list);
    }

    @Override
    public void appendItems(List<Gank> list) {
        for (Gank gank : list) {
            if (gank == null) continue;
            models.add(new ImageItem(gank));
        }
    }

    public static class ViewHolder extends BindHolder<ImageItem> {
        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.group_item)
        RelativeLayout groupItem;

        private final Context context;
        private final ItemClickListener itemClickListener;

        @NonNull
        private ArrayMap<String, Integer> heights;

        public ViewHolder(ViewGroup parent, ItemClickListener itemClickListener, @NonNull ArrayMap<String, Integer> heights) {
            super(parent, R.layout.adapter_meizi);
            this.context = parent.getContext();
            this.heights = heights;
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void bindHolder(ImageItem item) {
            final Gank gank = item.gank;
            final String url = gank.url;

            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new BitmapImageViewTarget(image) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            final int height = resource.getHeight();
                            final int width = resource.getWidth();
                            final int imageWidth = image.getWidth();
//                            Logcat.d(">>height:" + height + " width:" + width + " imageWidth:" + imageWidth);

                            int finalHeight = -1;
                            if (heights.containsKey(url)) {
                                Integer i = heights.get(url);
                                if (i != null) {
                                    finalHeight = i;
                                }
                            }

                            if (finalHeight == -1) {
                                finalHeight = height * imageWidth / width;
                                heights.put(url, finalHeight);
                            }

                            setCardViewLayoutParams(imageWidth, finalHeight);
                            super.onResourceReady(resource, transition);
                        }
                    });

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItem(image, url);
                }
            });
        }

        private void setCardViewLayoutParams(int width, int height) {
            ViewGroup.LayoutParams layoutParams = groupItem.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            groupItem.setLayoutParams(layoutParams);
        }
    }

    public static class ImageItem extends ItemModel {
        private Gank gank;

        ImageItem(Gank gank) {
            this.gank = gank;
        }

        @Override
        public int getViewType() {
            return 0;
        }
    }

    public interface ItemClickListener {
        void onItem(View view, String url);
    }
}
