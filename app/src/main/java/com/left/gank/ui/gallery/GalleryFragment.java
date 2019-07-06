package com.left.gank.ui.gallery;

import android.os.Bundle;
import android.ui.logcat.Logcat;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.left.gank.R;
import com.left.gank.rx.RxSchedulers;
import com.left.gank.ui.base.fragment.SupportFragment;
import com.left.gank.utils.RxSaveImage;
import com.left.gank.utils.ShareUtils;
import com.left.gank.widget.BottomSheetGalleryMenuDialog;
import com.left.gank.widget.ProgressImageView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * 图片浏览
 * Create by LingYan on 2016-12-19
 */
public class GalleryFragment extends SupportFragment {
    private static final String IMAGE_URL = "Image_Url";

    @BindView(R.id.progress_image)
    ProgressImageView progressImage;

    private String url;

    @Override
    protected int fragmentLayoutId() {
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
        progressImage.load(url, getContext());
        progressImage.setImageViewOnClick(imageViewOnClick);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private final ProgressImageView.ImageViewOnClick imageViewOnClick = new ProgressImageView.ImageViewOnClick() {
        @Override
        public void onImageClick(View v) {
            BottomSheetGalleryMenuDialog bottomSheetGalleryMenuDialog = new BottomSheetGalleryMenuDialog(getActivity());
            bottomSheetGalleryMenuDialog.setCallback(callback);
            bottomSheetGalleryMenuDialog.show();
        }

        @Override
        public void onLongClick(View v) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.view_gallery_menu);
            bottomSheetDialog.show();
        }
    };

    private final BottomSheetGalleryMenuDialog.Callback callback = new BottomSheetGalleryMenuDialog.Callback() {
        @Override
        public void share() {
            RxSaveImage.convertBitmap(getContext(), url)
                    .observeOn(RxSchedulers.INSTANCE.mainThread())
                    .map(bitmap -> {
                        File file = RxSaveImage.createImageFile(String.valueOf(bitmap.hashCode()));
                        if (file != null) {
                            return RxSaveImage.bitmapToImage(getContext(), bitmap, file);
                        }
                        return null;
                    })
                    .as(bindLifecycle())
                    .subscribe(uri -> ShareUtils.shareSingleImage(getContext(), uri), Logcat::e);
        }

        @Override
        public void save() {
            RxSaveImage.convertBitmap(getContext(), url)
                    .observeOn(RxSchedulers.INSTANCE.mainThread())
                    .map(bitmap -> {
                        File file = RxSaveImage.createImageFile(String.valueOf(bitmap.hashCode()));
                        if (file != null) {
                            return RxSaveImage.bitmapToImage(getContext(), bitmap, file);
                        }
                        return null;
                    })
                    .as(bindLifecycle())
                    .subscribe(uri -> shortToast(uri.toString()), Logcat::e);
        }
    };

    public static GalleryFragment newInstance(String url) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
}
