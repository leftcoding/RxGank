package com.left.gank.mvp.base;

import android.content.Context;
import android.left.ui.base.BaseView;

import com.leftcoding.rxbus.ObserverListener;
import com.leftcoding.rxbus.RxApiManager;

/**
 * Create by LingYan on 2019-04-28
 */
public abstract class ObserverPresenter<T extends BaseView> extends BasePresenter<T> {
    private ObserverListener observerListener;

    public ObserverPresenter(Context context, T view) {
        super(context, view);

    }

    protected void setObserverListener(ObserverListener observerListener) {
        this.observerListener = observerListener;
    }

    protected ObserverListener obtainObserver() {
        return observerListener == null ? RxApiManager.get() : observerListener;
    }

    protected void cleanDisposable(String tag) {
        if (observerListener != null) {
            observerListener.cleanDisposable(tag);
        }
    }
}
