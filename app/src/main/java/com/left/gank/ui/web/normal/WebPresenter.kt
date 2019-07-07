package com.left.gank.ui.web.normal

import android.rxbus.RxEventBus
import com.left.gank.data.entity.ReadHistory
import com.left.gank.data.entity.UrlCollect
import com.left.gank.domain.RxCollect
import com.left.gank.mvp.source.LocalDataSource
import com.left.gank.utils.ListUtils
import com.socks.library.KLog
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Create by LingYan on 2016-10-27
 */

class WebPresenter(private val mTask: LocalDataSource, private val mView: WebContract.View) : WebContract.Presenter {
    private var endTime: Long = 0
    private var mCollects: List<UrlCollect>? = null
    private var subscription: Disposable? = null

    private var isCollect: Boolean = false

    init {
        mCollects = ArrayList()
    }

    override fun findCollectUrl(url: String) {
        mTask.findUrlCollect(url).subscribe(object : Observer<List<UrlCollect>> {
            override fun onError(e: Throwable) {
                KLog.e(e)
            }

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(urlCollects: List<UrlCollect>) {
                mCollects = urlCollects
                val isCollect_ = ListUtils.getSize(urlCollects) > 0
                isCollect = isCollect_
                mView.setCollectIcon(isCollect_)
            }
        })
    }

    override fun insetHistoryUrl(readHistory: ReadHistory) {

    }

    override fun cancelCollect() {
        if (isCollect) {
            val deleteByKey = mCollects!![0].id!!
            mTask.cancelCollect(deleteByKey).subscribe(object : Observer<String> {
                override fun onError(e: Throwable) {
                    KLog.e(e)
                }

                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(string: String) {
                    isCollect = false

                    RxEventBus.newInstance().post(RxCollect(true))
                }
            })
        }
    }

    override fun collect() {
        if (!isCollect) {
        }
    }

    override fun collectAction(isCollect: Boolean) {
        KLog.d("isCollect:$isCollect")
        val curTime = System.currentTimeMillis()
        if (curTime - endTime < 2000) {
            subscription!!.dispose()
        }
        endTime = curTime

        subscription = Observable.create(ObservableOnSubscribe<Boolean> { subscriber ->
            subscriber.onNext(isCollect)
            subscriber.onComplete()
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1, TimeUnit.SECONDS)
                .subscribe { aBoolean ->
                    if (aBoolean!!) {
                        collect()
                    } else {
                        cancelCollect()
                    }
                }
    }

    fun destroy() {}
}
