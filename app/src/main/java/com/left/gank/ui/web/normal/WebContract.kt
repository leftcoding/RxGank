package com.left.gank.ui.web.normal

import android.ui.base.BaseView

import com.left.gank.data.entity.ReadHistory
import com.left.gank.data.entity.UrlCollect

/**
 * Create by LingYan on 2016-10-27
 */

interface WebContract {
    interface View : BaseView {

        val collect: UrlCollect
        fun onCollect()

        fun onCancelCollect()

        fun setCollectIcon(isCollect: Boolean)
    }

    interface Presenter {
        fun findCollectUrl(url: String)

        fun insetHistoryUrl(readHistory: ReadHistory)

        fun cancelCollect()

        fun collectAction(isCollect: Boolean)

        fun collect()
    }
}
