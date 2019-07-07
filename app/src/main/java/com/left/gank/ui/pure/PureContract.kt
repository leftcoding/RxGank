package com.left.gank.ui.pure

import android.business.domain.Gift
import android.content.Context

import com.left.gank.mvp.base.LoadMorePresenter
import com.left.gank.mvp.base.SupportView

/**
 * Create by LingYan on 2016-12-27
 */

interface PureContract {
    interface View : SupportView {
        fun loadDataSuccess(page: Int, list: List<Gift>)

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
