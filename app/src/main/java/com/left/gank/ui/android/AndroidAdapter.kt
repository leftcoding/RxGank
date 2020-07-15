package com.left.gank.ui.android

import android.business.domain.Solid
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.left.gank.R
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.adapter.FootAdapter
import com.left.gank.butterknife.item.ItemModel
import com.left.gank.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_android.view.*
import java.util.*

class AndroidAdapter(lifecycleOwner: LifecycleOwner) : FootAdapter<BindHolder<*>, List<Solid>>(lifecycleOwner) {
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

    override fun fillItems(list: List<Solid>?) {
        itemModels.clear()
        appendItems(list)
    }

    override fun appendItems(list: List<Solid>?) {
        if (list != null) {
            if (list.isEmpty()) {
                setEnd(true)
                return
            }
            for (solid in list) {
                itemModels.add(TextItem(solid))
            }
        } else {
            showError()
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class TextItem internal constructor(val solid: Solid) : ItemModel() {
        val time: String?
            get() {
                val date = DateUtils.formatToDate(solid.publishedAt)
                return DateUtils.formatString(date, DateUtils.MM_DD)
            }

        override fun getViewType(): Int {
            return TEXT
        }
    }

    internal class TextHolder(parent: ViewGroup, private val callback: Callback?) : BindHolder<TextItem>(parent, R.layout.adapter_android) {
        override fun bindHolder(item: TextItem) {
            val solid = item.solid
            itemView.time.text = item.time
            itemView.title.text = solid.desc
            itemView.author_name.text = solid.who
            itemView.setOnClickListener { v ->
                callback?.onItemClick(v, solid)
            }
        }
    }

    interface Callback {
        fun onItemClick(view: View, solid: Solid)
    }

    companion object {
        const val TEXT = 0
    }
}
