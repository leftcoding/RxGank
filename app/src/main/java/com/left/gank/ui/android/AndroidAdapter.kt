package com.left.gank.ui.android

import android.business.domain.Gank
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.left.gank.R
import com.left.gank.butterknife.adapter.FootAdapter
import com.left.gank.butterknife.holder.BindHolder
import com.left.gank.butterknife.item.ItemModel
import com.left.gank.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_android.view.*
import java.util.*

class AndroidAdapter(context: Context) : FootAdapter<BindHolder<*>, List<Gank>>(context) {
    private val itemModels = ArrayList<ItemModel>()
    private var callback: Callback? = null

    override fun rxCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<*>? {
        var bindHolder: BindHolder<*>? = null
        when (viewType) {
            TEXT -> bindHolder = TextHolder(parent, callback)
        }
        return bindHolder
    }

    override fun addItems(): List<ItemModel> {
        return itemModels
    }

    override fun fillItems(list: List<Gank>?) {
        itemModels.clear()
        appendItems(list)
    }

    override fun appendItems(list: List<Gank>?) {
        if (list != null) {
            if (list.isEmpty()) {
                setEnd(true)
                return
            }
            for (gank in list) {
                itemModels.add(TextItem(gank))
            }
        } else {
            showError()
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class TextItem internal constructor(val gank: Gank) : ItemModel() {
        val time: String?
            get() {
                val date = DateUtils.formatToDate(gank.publishedAt)
                return DateUtils.formatString(date, DateUtils.MM_DD)
            }

        override fun getViewType(): Int {
            return TEXT
        }
    }

    internal class TextHolder(parent: ViewGroup, private val callback: Callback?) : BindHolder<TextItem>(parent, R.layout.adapter_android) {
        override fun bindHolder(item: TextItem) {
            val gank = item.gank
            itemView.time.text = item.time
            itemView.title.text = gank.desc
            itemView.author_name.text = gank.who
            itemView.setOnClickListener { v ->
                callback?.onItemClick(v, gank)
            }
        }
    }

    interface Callback {
        fun onItemClick(view: View, gank: Gank)
    }

    companion object {
        const val TEXT = 0
    }
}
