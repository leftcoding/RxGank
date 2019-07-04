package com.left.gank.ui.index.tab;

import android.content.Context;
import android.ui.base.BaseView;

import com.left.gank.mvp.base.BasePresenter;

import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2017-09-28
 */

public interface MainTabContract {
    interface View extends BaseView {

    }

    public abstract class Presenter extends BasePresenter<View> {
        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        public abstract void loadPicture();
    }

}
