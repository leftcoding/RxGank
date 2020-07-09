package com.left.gank.ui.ios

import android.business.domain.Gank
import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.adapter.FootAdapter
import com.left.gank.butterknife.item.ItemModel
import com.left.gank.ui.ios.text.ItemCallback
import com.left.gank.ui.ios.text.TextHolder
import com.left.gank.ui.ios.text.TextModel
import com.left.gank.ui.ios.text.ViewType
import com.left.gank.utils.ListUtils
import java.util.*

/**
 * Create by LingYan on 2016-04-25
 */
internal class IosAdapter(context: Context, lifecycleOwner: LifecycleOwner) : FootAdapter<BindHolder<*>, List<Gank>>(context, lifecycleOwner) {
    private val itemModels = ArrayList<ItemModel>()

    private var itemCallback: ItemCallback? = null

    override fun addItems(): List<ItemModel> {
        return itemModels
    }

    override fun fillItems(list: List<Gank>) {
        itemModels.clear()
        appendItems(list)
    }

    override fun appendItems(list: List<Gank>) {
        val news = ArrayList<ItemModel>()
        if (ListUtils.isNotEmpty(list)) {
            for (gank in list) {
                news.add(TextModel(gank))
            }
        }
        itemModels.addAll(news)
    }

    override fun rxCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<*>? {
        var bindHolder: BindHolder<*>? = null
        when (viewType) {
            ViewType.NORMAL -> bindHolder = TextHolder(parent, itemCallback)
        }
        return bindHolder
    }

    fun setOnItemClickListener(itemCallBack: ItemCallback) {
        this.itemCallback = itemCallBack
    }
}
