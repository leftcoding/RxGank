package com.left.gank.ui.main;

import android.content.Context;
import androidx.annotation.NonNull;

import com.left.gank.mvp.base.BasePresenter;
import android.lectcoding.ui.base.BaseView;

/**
 * Create by LingYan on 2017-10-03
 */

public interface IndexContract {
    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

       public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }
    }
}
