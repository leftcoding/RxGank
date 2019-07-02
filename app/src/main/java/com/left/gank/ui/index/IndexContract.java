package com.left.gank.ui.index;

import android.content.Context;
import android.left.ui.base.BaseView;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.BasePresenter;

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
