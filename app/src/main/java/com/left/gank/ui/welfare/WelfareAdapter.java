package com.left.gank.ui.welfare;

import android.content.Context;
import android.graphics.Bitmap;
import android.ly.business.domain.Gank;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.left.gank.R;
import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.butterknife.holder.BindHolder;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.AppUtils;
import com.left.gank.utils.gilde.ImageLoaderUtil;
import com.left.gank.widget.ImageDefaultView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2018-09-25
 */
public class WelfareAdapter extends FootAdapter<WelfareAdapter.ViewHolder, List<Gank>> {
    private ArrayMap<String, Integer> heights = new ArrayMap<>();
    private List<ItemModel> models = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public WelfareAdapter(Context context) {
        super(context);
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

    public interface onResourceCallback {
        void onCallback(int width, int height);
    }

    private static class DriverViewTarget extends BitmapImageViewTarget {
        private final onResourceCallback resourceCallback;

        DriverViewTarget(ImageView image, onResourceCallback resourceCallback) {
            super(image);
            this.resourceCallback = resourceCallback;
        }

        @Override
        public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            super.onResourceReady(resource, transition);
            if (resourceCallback != null) {
                resourceCallback.onCallback(resource.getWidth(), resource.getHeight());
            }
        }
    }

    public static class ViewHolder extends BindHolder<ImageItem> {
        @BindView(R.id.img_meizi)
        ImageDefaultView imgMeizi;

        @BindView(R.id.meizi_card_view)
        RelativeLayout mRelativeLayout;

        ImageView imageView;

        private int screenWidth;
        private int screenHeight;

        private final Context context;
        private final ItemClickListener itemClickListener;
        private ArrayMap<String, Integer> heights;

        public ViewHolder(ViewGroup parent, ItemClickListener itemClickListener, ArrayMap<String, Integer> heights) {
            super(parent, R.layout.adapter_meizi);
            context = parent.getContext();
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMeizi.setFrameLayout(imageView);
            screenWidth = screenHeight = AppUtils.getDisplayWidth(context) / 2;
            this.heights = heights;
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void bindHolder(ImageItem item) {
            final Gank gank = item.gank;
            final String url = gank.url;

            RequestBuilder<Bitmap> requestBuilder = ImageLoaderUtil.getInstance()
                    .glideAsBitmap(context, url);

            if (heights.containsKey(url)) {
                setCardViewLayoutParams(imgMeizi, screenWidth, heights.get(url));
                requestBuilder.apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                );
                requestBuilder.into(imageView);
            } else {
                requestBuilder.apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .override(screenWidth, screenHeight)//设置宽高一致，后期改动不大
                );
                requestBuilder.into(new DriverViewTarget(imageView, new onResourceCallback() {
                    @Override
                    public void onCallback(int width, int height) {
                        if (!heights.containsKey(url) && url != null) {
                            int viewHeight = width * screenWidth / height;
                            heights.put(url, viewHeight);
                            setCardViewLayoutParams(imgMeizi, screenWidth, heights.get(url));
                        }
                    }
                }));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItem(imgMeizi, getLayoutPosition());
                    }
                }
            });
        }

        private void setCardViewLayoutParams(ImageDefaultView imageDefaultView, int width, int height) {
            ViewGroup.LayoutParams layoutParams = imageDefaultView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            imageDefaultView.setLayoutParams(layoutParams);
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
