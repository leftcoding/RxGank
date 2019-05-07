package com.left.gank.ui.welfare;

import android.content.Context;
import android.graphics.Bitmap;
import android.ly.business.domain.Gank;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.left.gank.R;
import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * Create by LingYan on 2018-09-25
 */
public class WelfareAdapter extends FootAdapter<WelfareAdapter.ViewHolder, List<Gank>> {
    private static ArrayMap<String, Integer> heights = new ArrayMap<>();
    private List<ItemModel> models = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public WelfareAdapter(Context context) {
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
            context = parent.getContext();
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
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_default)
                    .error(R.drawable.ic_image_default)
                    .fallback(R.drawable.ic_image_default)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            final int height = resource.getHeight();
                            final int width = resource.getWidth();
                            final int imageWidth = image.getWidth();

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
                            return false;
                        }
                    })
                    .into(image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItem(image, getAdapterPosition());
                    }
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
        void onItem(View view, int position);
    }
}
