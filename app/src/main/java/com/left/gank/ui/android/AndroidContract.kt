package com.left.gank.ui.android

import android.business.domain.Gank
import android.content.Context

import com.left.gank.mvp.base.ObserverPresenter
import com.left.gank.mvp.base.SupportView

/**
 * Create by LingYan on 2016-10-25
 */

interface AndroidContract {
    interface View : SupportView {
        fun loadAndroidSuccess(page: Int, list: List<Gank>)

        fun loadAndroidFailure(page: Int, msg: String)
    }

    abstract class Presenter(context: Context, view: View) : ObserverPresenter<View>(context, view) {

        abstract fun loadAndroid(refresh: Boolean, useProgress: Boolean, page: Int)
    }
}
