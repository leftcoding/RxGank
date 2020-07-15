package com.left.gank.ui.ios.text

import android.business.domain.Solid
import com.left.gank.butterknife.item.ItemModel
import com.left.gank.utils.DateUtils

/**
 * Create by LingYan on 2018-11-12
 */
class TextModel(internal val solid: Solid) : ItemModel() {

    val time: String?
        get() {
            val date = DateUtils.formatToDate(solid.publishedAt)
            return DateUtils.formatString(date, DateUtils.MM_DD)
        }

    override fun getViewType(): Int {
        return ViewType.NORMAL
    }
}
