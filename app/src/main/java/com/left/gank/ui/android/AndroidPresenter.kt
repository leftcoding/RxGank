package com.left.gank.ui.android

import android.business.api.GankServer
import android.business.domain.Gank
import android.business.domain.PageEntity
import android.business.observer.ManagerObserver
import android.content.Context
import com.left.gank.ui.android.AndroidContract.Presenter

/**
 * Create by LingYan on 2016-10-25
 */
internal class AndroidPresenter(context: Context, view: AndroidContract.View) : Presenter(context, view) {

    override fun loadAndroid(refresh: Boolean, useProgress: Boolean, page: Int) {
        if (isDestroy) {
            return
        }

        GankServer.with()
                .api()
                .androids(refresh, page, INIT_LIMIT)
                .doOnSubscribe { showProgress(useProgress) }
                .doFinally { hideProgress() }
                .subscribe(object : ManagerObserver<PageEntity<Gank>>(requestTag, obtainObserver()) {
                    override fun onSuccess(entity: PageEntity<Gank>?) {
                        if (isViewLife) {
                            if (entity != null) {
                                view.loadAndroidSuccess(page, entity.results)
                                return
                            }
                            view.loadAndroidFailure(page, errorTip)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        e.printStackTrace()
                        if (isViewLife) {
                            view.loadAndroidFailure(page, errorTip)
                        }
                    }
                })
    }

    override fun onDestroy() {
        cleanDisposable(requestTag)
    }

    companion object {
        // 请求个数
        private const val INIT_LIMIT = 20
    }
}
