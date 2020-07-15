package com.left.gank.ui.ios

import android.business.api.GankServer
import android.business.domain.PageEntity
import android.business.domain.Solid
import android.business.observer.ManagerObserver
import android.content.Context
import com.uber.autodispose.ObservableSubscribeProxy
import retrofit2.Response

/**
 * Create by LingYan on 2016-12-20
 */

class IosPresenter(context: Context, view: IosContract.View) : IosContract.Presenter(context, view) {

    override fun loadIos(refresh: Boolean, useProgress: Boolean, page: Int) {
        if (isDestroy) {
            return
        }
        GankServer.with()
                .api()
                .ios(refresh, page, INIT_LIMIT)
                .doOnSubscribe { showProgress(useProgress) }
                .doFinally { hideProgress() }
                .`as`<ObservableSubscribeProxy<Response<PageEntity<Solid>>>>(bindLifecycle<Response<PageEntity<Solid>>>())
                .subscribe(object : ManagerObserver<PageEntity<Solid>>(requestTag, obtainObserver()) {
                    override fun onSuccess(entity: PageEntity<Solid>?) {
                        if (isViewLife) {
                            if (entity != null) {
                                view.loadIosSuccess(page, entity.results)
                                return
                            }
                            view.loadIosFailure(page, errorTip)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        if (isViewLife) {
                            view.loadIosFailure(page, errorTip)
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
