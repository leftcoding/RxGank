package com.left.gank.ui.gallery

import android.business.domain.Solid
import android.content.Context

import com.left.gank.mvp.base.LoadMorePresenter
import com.left.gank.mvp.base.SupportView

/**
 * Create by LingYan on 2017-01-16
 */

interface GalleryContract {

    interface View : SupportView {
        fun appendData(list: List<Solid>?)
    }

    abstract class Presenter(context: Context, view: View) : LoadMorePresenter<View>(context, view)
}
