package com.left.gank.mvp.observer;

import android.ly.business.domain.BaseEntity;

import retrofit2.Response;

/**
 * Create by LingYan on 2017-11-19
 */

public abstract class RefreshOnObserver<T extends BaseEntity> extends BaseObserver<Response<T>> {

    public RefreshOnObserver(String requestTag) {
        super(requestTag);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure(Throwable e, String msg);

    @Override
    public void onNext(Response<T> t) {
        if (t == null) {
            onFailure(new Throwable("response is null"), "请求数据失败");
            return;
        }
        if (!t.isSuccessful()) {
            onFailure(new Throwable("request code is:" + t.code()), "请求数据失败");
            return;
        }
        onSuccess(t.body());
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        onFailure(e, "");
    }

    @Override
    public void onComplete() {
        // nothing
    }
}
