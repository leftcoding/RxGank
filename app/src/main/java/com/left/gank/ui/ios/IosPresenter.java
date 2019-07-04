package com.left.gank.ui.ios;

import android.business.api.GankServer;
import android.business.domain.Gank;
import android.business.domain.PageEntity;
import android.business.observer.ManagerObserver;
import android.content.Context;

/**
 * Create by LingYan on 2016-12-20
 */

public class IosPresenter extends IosContract.Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;

    public IosPresenter(Context context, IosContract.View view) {
        super(context, view);
    }

    @Override
    void loadIos(boolean refresh, boolean useProgress, int page) {
        if (isDestroy()) {
            return;
        }
        GankServer.with()
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
