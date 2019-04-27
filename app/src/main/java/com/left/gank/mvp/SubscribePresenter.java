package com.left.gank.mvp;

import android.content.Context;
import androidx.annotation.NonNull;

import com.left.gank.mvp.base.BasePresenter;
import android.lectcoding.ui.base.BaseView;

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
