package com.left.gank.ui.discovered.video;

import android.business.domain.Gank;
import android.content.Context;

import com.left.gank.bean.GankResult;
import com.left.gank.config.MeiziArrayList;
import com.left.gank.mvp.source.remote.GankDataSource;
import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-01-03
 */

public class VideoPresenter extends VideoContract.Presenter {
    private GankDataSource mTask;
    private VideoContract.View mModelView;

    VideoPresenter(Context context, VideoContract.View view) {
        super(context, view);
    }

    private void fetchData(int page) {
        Observable<GankResult> observable;
        if (MeiziArrayList.getInstance().isOneItemsEmpty()) {
            observable = mTask.fetchVideoAndImages(page, 20);
        } else {
            observable = mTask.fetchVideo(page, 20);
        }

        observable.subscribe(new Observer<GankResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GankResult gankResult) {
                List<Gank> list = gankResult.getResults(), mModelView;
            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

    }
}
