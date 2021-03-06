package com.left.gank.mvp.base;

import android.content.Context;
import android.ui.base.BaseContract;
import android.ui.base.BaseView;

import com.left.gank.R;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by LingYan on 2016-05-12
 */
public abstract class BasePresenter<E extends BaseView> extends BaseContract.Presenter {
    protected E view;
    protected Context context;
    protected String requestTag = UUID.randomUUID().toString();
    private AtomicBoolean isDestroy = new AtomicBoolean(false);
    protected String errorTip;

    public BasePresenter(Context context, E view) {
        this.view = view;
        this.context = context;
        errorTip = context.getString(R.string.loading_error);
    }

    public void destroy() {
        onDestroy();
        isDestroy.set(true);
        view = null;
        context = null;
    }

    protected void showProgress() {
        showProgress(true);
    }

    protected void showProgress(boolean useProgress) {
        if (useProgress && isViewLife()) {
            view.showProgress();
        }
    }

    protected void hideProgress() {
        if (isViewLife()) {
            view.hideProgress();
        }
    }

    protected boolean isViewLife() {
        return view != null;
    }

    protected abstract void onDestroy();

    protected boolean isDestroy() {
        return isDestroy.get();
    }
}
