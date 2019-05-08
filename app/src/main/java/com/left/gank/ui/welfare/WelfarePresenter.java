package com.left.gank.ui.welfare;

import android.content.Context;
import android.ly.business.api.GankServer;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;
import android.ly.business.observer.ManagerObserver;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Create by LingYan on 2016-12-23
 */

public class WelfarePresenter extends WelfareContract.Presenter {
    private static final int DEFAULT_LIMIT = 20;

    WelfarePresenter(Context context, WelfareContract.View view) {
        super(context, view);
    }

    @Override
    public void loadWelfare(boolean refresh, final boolean useProgress, final int page) {
        GankServer.with()
                .api()
                .images(refresh, page, DEFAULT_LIMIT)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showProgress(useProgress);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideProgress();
                    }
                })
                .subscribe(new ManagerObserver<PageEntity<Gank>>(requestTag, obtainObserver()) {
                    @Override
                    protected void onSuccess(PageEntity<Gank> entity) {
                        if (isViewLife()) {
                            if (entity != null) {
                                view.loadWelfareSuccess(page, entity.results);
                                return;
                            }
                            view.loadWelfareFailure(page, errorTip);
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        e.printStackTrace();
                        if (isViewLife()) {
                            view.loadWelfareFailure(page, errorTip);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        cleanDisposable(requestTag);
    }
}
