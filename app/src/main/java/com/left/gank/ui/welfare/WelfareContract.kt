package com.left.gank.ui.welfare

import android.business.domain.Solid
import android.content.Context
import android.ui.base.BaseView

import com.left.gank.mvp.base.LoadMorePresenter


/**
 * Create by LingYan on 2016-12-23
 */

interface WelfareContract {
    interface View : BaseView {
        fun loadWelfareSuccess(page: Int, list: List<Solid>?)

        fun loadWelfareFailure(page: Int, msg: String)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view) {

        abstract fun loadWelfare(refresh: Boolean, useProgress: Boolean, page: Int)
    }
}
