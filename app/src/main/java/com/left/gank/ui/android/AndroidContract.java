package com.left.gank.ui.android;

import android.content.Context;
import android.ly.business.domain.Gank;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.PageView;

/**
 * Create by LingYan on 2016-10-25
 */

public interface AndroidContract {
    interface View extends PageView<Gank> {
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        public abstract void loadAndroid(boolean refresh, boolean useProgress, int page);
    }
}
