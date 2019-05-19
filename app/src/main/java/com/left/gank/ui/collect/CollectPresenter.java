package com.left.gank.ui.collect;

import com.left.gank.data.entity.UrlCollect;
import com.left.gank.mvp.source.LocalDataSource;
import com.left.gank.utils.ListUtils;
import com.socks.library.KLog;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Create by LingYan on 2016-05-12
 */
public class CollectPresenter extends CollectContract.Presenter {
    private static final int LIMIT = 10;

    @NonNull
    private LocalDataSource localDataSource;

    @NonNull
    private CollectContract.View view;

    private int page = 0;
    private boolean isNoMore;

    public CollectPresenter(@NonNull LocalDataSource task, @NonNull CollectContract.View modelView) {
        this.localDataSource = task;
        this.view = modelView;
    }

    private void parseData(List<UrlCollect> list) {
        int size = ListUtils.getSize(list);
        if (size > 0) {
            if (page == 0) {
                view.setAdapterList(list);
            } else {
                view.appendAdapter(list);
            }
            view.showContent();

            if (size < LIMIT) {
                isNoMore = true;
            }
        } else {
            if (page > 0) {
            } else {
                view.showEmpty();
            }
        }
    }

    @Override
    public void fetchNew() {
        isNoMore = false;
        page = 0;
        int offset = getOffset();
        getCollects(offset);
    }

    @Override
    public void fetchMore() {
        if (!isNoMore) {
            view.showProgress();
            int offset = getOffset();
            getCollects(offset);
        }
    }

    private int getOffset() {
        return page * LIMIT;
    }

    private void getCollects(int offset) {
        localDataSource.getCollect(offset, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UrlCollect>>() {
                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                        page = page + 1;
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UrlCollect> urlCollects) {
                        parseData(urlCollects);
                    }
                });
    }

    @Override
    public void cancelCollect(final long position) {
    }

    @Override
    public void insertCollect(UrlCollect collect) {
        localDataSource.insertCollect(collect).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                view.revokeCollect();
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

    public void destroy() {

    }
}
