package com.left.gank.ui.index.tab;

import android.content.Context;
import androidx.annotation.NonNull;

import android.lectcoding.ui.base.BaseView;
import com.left.gank.mvp.base.BasePresenter;

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
