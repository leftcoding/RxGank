package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.ObserverPresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-20
 */

public interface IosContract {
    interface View extends SupportView {
        void loadIosSuccess(List<Gank> list);

        void loadIosFailure(String msg);
    }

    abstract class Presenter extends ObserverPresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void loadIos(final boolean refresh, final boolean useProgress, int page);
    }
}
