package com.left.gank.ui.gallery;

import android.content.Context;

import com.left.gank.config.MeiziArrayList;
import com.left.gank.domain.GankResult;
import com.left.gank.mvp.source.remote.GankDataSource;
import com.left.gank.utils.CrashUtils;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-01-16
 */

public class GalleryPresenter extends GalleryContract.Presenter {
    private GankDataSource mTask;
    private GalleryContract.View mModelView;
    private boolean isFetch;

    public GalleryPresenter(Context context, GalleryContract.View view) {
        super(context, view);
    }

    public void fetchMore() {
        if (!isFetch) {
            int nextPage = MeiziArrayList.getInstance().getPage() + 1;
            mTask.fetchWelfare(nextPage,20)
                    .subscribe(new Observer<GankResult>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            isFetch = true;
                        }

                        @Override
                        public void onNext(GankResult gankResult) {
//                            List<Gank> list = filterData(gankResult.getResults(), mModelView);
//                            if (ListUtils.getSize(list) > 0) {
//                                mModelView.appendData(list);
//                                mModelView.sysNumText();
//                                MeiziArrayList.getInstance().addImages(gankResult.getResults(), nextPage);
//                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            KLog.e(e);
                            CrashUtils.crashReport(e);
//                            parseError(mModelView);
                        }

                        @Override
                        public void onComplete() {
                            isFetch = false;
                        }
                    });
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void onDestroy() {

    }
}
