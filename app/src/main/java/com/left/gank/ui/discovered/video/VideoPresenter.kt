package com.left.gank.ui.discovered.video

import android.business.api.GankServer
import android.business.domain.PageEntity
import android.business.domain.Solid
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
                .`as`<ObservableSubscribeProxy<Response<PageEntity<Solid>>>>(bindLifecycle<Response<PageEntity<Solid>>>())
                .subscribe(object : ManagerObserver<PageEntity<Solid>>(requestTag, obtainObserver()) {
                    override fun onSuccess(entity: PageEntity<Solid>?) {
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
