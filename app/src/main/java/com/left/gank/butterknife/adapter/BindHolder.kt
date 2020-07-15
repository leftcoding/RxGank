package com.left.gank.butterknife.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import com.left.gank.butterknife.item.ItemModel

/**
 * Create by LingYan on 2017-10-13
 */
abstract class BindHolder<in T : ItemModel>(parent: ViewGroup?, @LayoutRes layout: Int) : BasicHolder(parent, layout) {
    init {
        ButterKnife.bind(this, itemView)
    }

    open fun bindHolder(item: T) {}
}