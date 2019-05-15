package com.left.gank.ui.gallery;

import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.ui.base.fragment.SupportFragment;
import com.left.gank.widget.ProgressImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * 图片浏览
 * Create by LingYan on 2016-12-19
 */
public class GalleryFragment extends SupportFragment implements ProgressImageView.ImageViewOnClick {
    private static final String IMAGE_URL = "Image_Url";

    @BindView(R.id.progress_image)
    ProgressImageView progressImage;

    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_picture;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString(IMAGE_URL);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressImage.load(url, getContext().getApplicationContext());
        progressImage.setImageViewOnClick(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static GalleryFragment newInstance(String url) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onImageClick(View v) {
    }
}
