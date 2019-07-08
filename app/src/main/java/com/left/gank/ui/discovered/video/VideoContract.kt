package com.left.gank.ui.discovered.video

import android.business.domain.Gank
import android.content.Context

import com.left.gank.mvp.base.LoadMorePresenter
import com.left.gank.mvp.base.SupportView


/**
 * Create by LingYan on 2017-01-03
 */

interface VideoContract {
    interface View : SupportView {
        fun loadVideoSuccess(page: Int, list: List<Gank>)

        fun loadVideoFailure(page: Int, msg: String)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view) {

        abstract fun loadVideo(refresh: Boolean, useProgress: Boolean, page: Int)
    }
}

