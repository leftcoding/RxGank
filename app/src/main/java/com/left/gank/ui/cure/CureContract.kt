package com.left.gank.ui.cure

import android.business.domain.Gift
import android.content.Context

import com.left.gank.mvp.base.LoadMorePresenter

/**
 * Create by LingYan on 2016-10-26
 */

interface CureContract {
    interface View : com.left.gank.mvp.base.SupportView {
        fun loadDataSuccess(page: Int, maxPage: Int, list: List<Gift>)

        fun loadDataFailure(page: Int, msg: String)

        fun showLoadingDialog()

        fun hideLoadingDialog()

        fun loadImagesSuccess(list: List<Gift>)

        fun loadImagesFailure(msg: String)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view) {

        internal abstract fun loadData(page: Int)

        internal abstract fun loadImages(url: String)
    }
}
