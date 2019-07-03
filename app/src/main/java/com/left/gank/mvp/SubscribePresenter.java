package com.left.gank.mvp;

import android.content.Context;
import android.ui.base.BaseView;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.BasePresenter;

/**
 * Create by LingYan on 2017-10-05
 */

public class SubscribePresenter<E extends BaseView> extends BasePresenter<E> {

    public SubscribePresenter(@NonNull Context context, @NonNull E view) {
        super(context, view);
    }

    @Override
    protected void onDestroy() {

    }
}
