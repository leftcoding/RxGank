package com.left.gank.ui.welfare

import android.business.api.GankServer
import android.business.domain.Gank
import android.business.domain.PageEntity
import android.business.observer.ManagerObserver
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import retrofit2.Response

/**
 * Create by LingYan on 2016-12-23
 */

class WelfarePresenter internal constructor(context: Context, view: WelfareContract.View) : WelfareContract.Presenter(context, view) {

    override fun loadWelfare(refresh: Boolean, useProgress: Boolean, page: Int) {
        GankServer.with()
                .api()
                .images(refresh, page, DEFAULT_LIMIT)
                .doOnSubscribe { showProgress(useProgress) }
                .doFinally { hideProgress() }
                .`as`<ObservableSubscribeProxy<Response<PageEntity<Gank>>>>(AutoDispose.autoDisposable<Response<PageEntity<Gank>>>(AndroidLifecycleScopeProvider.from(view as LifecycleOwner)))
                .subscribe(object : ManagerObserver<PageEntity<Gank>>(requestTag, obtainObserver()) {
                    override fun onSuccess(entity: PageEntity<Gank>?) {
                        if (isViewLife) {
                            if (entity != null) {
                                view.loadWelfareSuccess(page, entity.results)
                                return
                            }
                            view.loadWelfareFailure(page, errorTip)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        e.printStackTrace()
                        if (isViewLife) {
                            view.loadWelfareFailure(page, errorTip)
                        }
                    }
                })
    }

    override fun onDestroy() {
        cleanDisposable(requestTag)
    }

    companion object {
        private const val DEFAULT_LIMIT = 20
    }
}
