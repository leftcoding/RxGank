package com.left.gank.ui.gallery

import android.content.Context

import com.left.gank.config.MeiziArrayList
import com.left.gank.domain.GankResult
import com.left.gank.mvp.source.remote.GankDataSource
import com.left.gank.utils.CrashUtils
import com.socks.library.KLog

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Create by LingYan on 2017-01-16
 */

class GalleryPresenter(context: Context, view: GalleryContract.View) : GalleryContract.Presenter(context, view) {
    private val mTask: GankDataSource? = null
    private val mModelView: GalleryContract.View? = null
    private var isFetch: Boolean = false

    fun fetchMore() {
        if (!isFetch) {
            val nextPage = MeiziArrayList.getInstance().page + 1
            mTask!!.fetchWelfare(nextPage, 20)
                    .subscribe(object : Observer<GankResult> {
                        override fun onSubscribe(d: Disposable) {
                            isFetch = true
                        }

                        override fun onNext(gankResult: GankResult) {
                            //                            List<Gank> list = filterData(gankResult.getResults(), mModelView);
                            //                            if (ListUtils.getSize(list) > 0) {
                            //                                mModelView.appendData(list);
                            //                                mModelView.sysNumText();
                            //                                MeiziArrayList.getInstance().addImages(gankResult.getResults(), nextPage);
                            //                            }
                        }

                        override fun onError(e: Throwable) {
                            KLog.e(e)
                            CrashUtils.crashReport(e)
                            //                            parseError(mModelView);
                        }

                        override fun onComplete() {
                            isFetch = false
                        }
                    })
        }
    }

    override fun onDestroy() {

    }
}
