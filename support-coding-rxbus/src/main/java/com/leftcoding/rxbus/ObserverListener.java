package com.leftcoding.rxbus;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2019-04-28
 */
public interface ObserverListener {
    void addDisposable(String tag, Disposable disposable);

    void cleanDisposable(String tag);

    void clearAll();
}
