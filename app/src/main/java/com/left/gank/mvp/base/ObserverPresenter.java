package com.left.gank.mvp.base;

import android.content.Context;
import android.rxbus.ObserverListener;
import android.rxbus.RxApiManager;
import android.ui.base.BaseView;

import androidx.lifecycle.LifecycleOwner;

import com.left.gank.utils.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * Create by LingYan on 2019-04-28
 */
public abstract class ObserverPresenter<T extends BaseView> extends BasePresenter<T> {
    private ObserverListener observerListener;
    protected LifecycleOwner lifeCycleOwner;

    public void setLifeCycleOwner(LifecycleOwner lifeCycleOwner) {
        this.lifeCycleOwner = lifeCycleOwner;
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(lifeCycleOwner);
    }

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
            observerListener.remove(tag);
        }
    }
}
