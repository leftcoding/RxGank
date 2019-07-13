package com.left.gank.ui.daily

import android.business.domain.Girl
import android.content.Context

import com.left.gank.mvp.base.LoadMorePresenter
import com.left.gank.mvp.base.SupportView


/**
 * Create by LingYan on 2016-10-26
 */

interface DailyGirlContract {
    interface View : SupportView {
        fun refillData(list: List<Girl>)

        fun appendItem(list: List<Girl>)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view) {
        internal abstract fun loadGirls(page: Int)
    }
}
