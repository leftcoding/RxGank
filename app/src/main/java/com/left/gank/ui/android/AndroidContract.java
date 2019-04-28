package com.left.gank.ui.android;

import android.content.Context;
import android.ly.business.domain.Gank;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.ObserverPresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-25
 */

public interface AndroidContract {
    interface View extends SupportView {
        void loadAndroidSuccess(int page, List<Gank> list);

        void loadAndroidFailure(int page, String msg);
    }

    abstract class Presenter extends ObserverPresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        public abstract void loadAndroid(boolean refresh, boolean useProgress, int page);
    }
}
