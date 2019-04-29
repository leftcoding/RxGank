package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;
import android.ly.business.observer.ManagerObserver;

import androidx.annotation.NonNull;


/**
 * Create by LingYan on 2016-12-20
 */

public class IosPresenter extends IosContract.Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;

    public IosPresenter(@NonNull Context context, IosContract.View view) {
        super(context, view);
    }

    @Override
    void loadIos(boolean refresh, boolean useProgress, int page) {
        if (isDestroy()) {
            return;
        }
        GankServer.with(context)
                .api()
                .ios(refresh, page, INIT_LIMIT)
                .doOnSubscribe(disposable -> {
                    showProgress(useProgress);
                })
                .doFinally(() -> {
                    hideProgress();
                })
                .subscribe(new ManagerObserver<PageEntity<Gank>>(requestTag, obtainObserver()) {
                    @Override
                    protected void onSuccess(PageEntity<Gank> entity) {
                        if (isViewLife()) {
                            if (entity != null) {
                                view.loadIosSuccess(page, entity.results);
                                return;
                            }
                            view.loadIosFailure(page, errorTip);
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        if (isViewLife()) {
                            view.loadIosFailure(page, errorTip);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        cleanDisposable(requestTag);
    }
}
