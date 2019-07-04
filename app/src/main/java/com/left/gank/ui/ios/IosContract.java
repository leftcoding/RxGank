package com.left.gank.ui.ios;

import android.business.domain.Gank;
import android.content.Context;

import com.left.gank.mvp.base.ObserverPresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2016-12-20
 */

public interface IosContract {
    interface View extends SupportView {
        void loadIosSuccess(int page, List<Gank> list);

        void loadIosFailure(int page, String msg);
    }

    abstract class Presenter extends ObserverPresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void loadIos(final boolean refresh, final boolean useProgress, int page);
    }
}
