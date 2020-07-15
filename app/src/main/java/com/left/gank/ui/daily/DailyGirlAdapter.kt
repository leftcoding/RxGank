package com.left.gank.ui.daily

import android.business.domain.Girl
import android.view.ViewGroup
import com.left.gank.R
import com.left.gank.butterknife.adapter.BaseAdapter
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.item.ItemModel
import kotlinx.android.synthetic.main.adapter_daily_girl.view.*

/**
 * Create by LingYan on 2016-07-05
 */
class DailyGirlAdapter internal constructor() : BaseAdapter<BindHolder<ItemModel>>() {
    private var itemClick: ItemCallback? = null
    private val items = mutableListOf<ItemModel>()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<ItemModel> {
        return DailyGirlHolder(parent, itemClick) as BindHolder<ItemModel>
    }

    override fun onBindViewHolder(holder: BindHolder<ItemModel>, position: Int) {
        holder.bindHolder(items[position])
    }

    fun setOnItemClickListener(itemClick: ItemCallback) {
        this.itemClick = itemClick
    }

    override fun getItemCount(): Int = items.size

    fun refillItem(girlList: List<Girl?>?) {
        items.clear()
        girlList?.filterNotNull()?.forEach {
            items.add(NormalItem(it))
        }
    }

    class DailyGirlHolder(parent: ViewGroup, private val call: ItemCallback?) : BindHolder<NormalItem>(parent, R.layout.adapter_daily_girl) {

        override fun bindHolder(item: NormalItem) {
            val girl = item.girl
            itemView.title.apply {
                text = girl.title
            }

            itemView.setOnClickListener { call?.onItemClick(girl) }
        }
    }

    class NormalItem(val girl: Girl) : ItemModel() {

        override fun getViewType(): Int {
            return 0
        }
    }

    interface ItemCallback {
        fun onItemClick(girl: Girl)
    }
}
