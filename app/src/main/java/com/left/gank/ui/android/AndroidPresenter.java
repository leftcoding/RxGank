package com.left.gank.ui.android;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;
import android.ly.business.observer.ResponseObserver;

import com.left.gank.ui.android.AndroidContract.Presenter;

/**
 * Create by LingYan on 2016-10-25
 */
class AndroidPresenter extends Presenter {
    // 请求个数
    private static final int INIT_LIMIT = 20;

    AndroidPresenter(Context context, AndroidContract.View view) {
        super(context, view);
    }

    @Override
    public void loadAndroid(final boolean refresh, final boolean useProgress, int page) {
        if (isDestroy()) {
            return;
        }

        GankServer.with(context)
                .api()
                .androids(false, page, INIT_LIMIT)
                .doOnSubscribe(disposable -> {
                    showProgress(useProgress);
                })
                .doFinally(() -> {
                    hideProgress();
                })
                .subscribe(new ResponseObserver<PageEntity<Gank>>(requestTag) {
                    @Override
                    protected void onSuccess(PageEntity<Gank> entity) {
                        if (isViewLife()) {
                            if (entity != null) {
                                view.refreshSuccess(entity.results);
                                return;
                            }
                            view.refreshFailure("");
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        e.printStackTrace();
                        if (isViewLife()) {
                            view.refreshFailure(e.toString());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        GankServer.with(context).clean(requestTag);
    }
}
