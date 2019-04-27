package com.left.gank.mvp.subseribe;

import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import android.ly.business.observer.ResponseObserver;

/**
 * Create by LingYan on 2018-03-22
 */

public class PageSubscribe extends ResponseObserver<PageEntity<Gank>> {
    public PageSubscribe(String requestTag) {
        super(requestTag);
    }

    @Override
    protected void onSuccess(PageEntity<Gank> gankPageEntity) {

    }

    @Override
    protected void onFailure(Throwable e, String msg) {

    }
}