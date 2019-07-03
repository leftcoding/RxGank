package com.left.gank.ui.gallery;

import android.business.domain.Gank;
import android.content.Context;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2017-01-16
 */

public interface GalleryContract {

    interface View extends SupportView {
        void appendData(List<Gank> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
