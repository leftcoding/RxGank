package com.left.gank.mvp.base;

import android.content.Context;
import android.ui.base.BaseView;

import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class LoadMorePresenter<E extends BaseView> extends ObserverPresenter<E> {

    public LoadMorePresenter(@NonNull Context context, E view) {
        super(context, view);
    }
}
