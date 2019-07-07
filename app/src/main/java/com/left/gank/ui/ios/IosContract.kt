package com.left.gank.ui.ios

import android.business.domain.Gank
import android.content.Context

import com.left.gank.mvp.base.ObserverPresenter
import com.left.gank.mvp.base.SupportView

/**
 * Create by LingYan on 2016-12-20
 */

interface IosContract {
    interface View : SupportView {
        fun loadIosSuccess(page: Int, list: List<Gank>?)

        fun loadIosFailure(page: Int, msg: String)
    }

    abstract class Presenter(context: Context, view: View) : ObserverPresenter<View>(context, view) {
        internal abstract fun loadIos(refresh: Boolean, useProgress: Boolean, page: Int)
    }
}
