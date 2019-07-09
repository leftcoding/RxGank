package com.left.gank.ui.discovered.jiandan

import android.content.Context

import com.left.gank.domain.JianDanBean
import com.left.gank.mvp.base.LoadMorePresenter
import com.left.gank.mvp.base.SupportView

/**
 * Create by LingYan on 2016-11-21
 */

interface JiandanContract {
    interface View : SupportView {
        fun loadJiandanSuccess(page: Int, list: List<JianDanBean>?)

        fun loadJiandanFailure(page: Int, msg: String)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view) {
        abstract fun loadJianDan(page: Int)
    }
}
