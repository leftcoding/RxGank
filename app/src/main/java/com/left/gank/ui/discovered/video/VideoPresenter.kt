package com.left.gank.ui.discovered.video

import android.business.api.GankServer
import android.business.domain.Gank
import android.business.domain.PageEntity
import android.business.observer.ManagerObserver
import android.content.Context
import com.uber.autodispose.ObservableSubscribeProxy
import retrofit2.Response

/**
 * Create by LingYan on 2017-01-03
 */

class VideoPresenter internal constructor(context: Context, view: VideoContract.View) : VideoContract.Presenter(context, view) {

    override fun loadVideo(refresh: Boolean, useProgress: Boolean, page: Int) {
        if (isDestroy) {
            return
        }

        GankServer.with()
                .api()
                .video(refresh, page, INIT_LIMIT)
                .doOnSubscribe { showProgress(useProgress) }
                .doFinally { hideProgress() }
                .`as`<ObservableSubscribeProxy<Response<PageEntity<Gank>>>>(bindLifecycle<Response<PageEntity<Gank>>>())
                .subscribe(object : ManagerObserver<PageEntity<Gank>>(requestTag, obtainObserver()) {
                    override fun onSuccess(entity: PageEntity<Gank>?) {
                        if (isViewLife) {
                            if (entity != null) {
                                view.loadVideoSuccess(page, entity.results)
                                return
                            }
                            view.loadVideoFailure(page, errorTip)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        e.printStackTrace()
                        if (isViewLife) {
                            view.loadVideoFailure(page, errorTip)
                        }
                    }
                })
    }

    override fun onDestroy() {

    }

    companion object {
        private const val INIT_LIMIT = 20
    }
}
