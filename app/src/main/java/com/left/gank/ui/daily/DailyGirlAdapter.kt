package com.left.gank.ui.daily

import android.business.domain.Girl
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.left.gank.R
import com.left.gank.butterknife.adapter.BaseAdapter
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.item.ItemModel
import kotlinx.android.synthetic.main.adapter_daily_girl.view.*

/**
 * Create by LingYan on 2016-07-05
 */
class DailyGirlAdapter internal constructor() : BaseAdapter<BindHolder<ItemModel>>() {
    private var itemClick: ItemClickListener? = null
    private val items = mutableListOf<ItemModel>()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<ItemModel> {
        return DailyGirlHolder(parent, itemClick) as BindHolder<ItemModel>
    }

    override fun onBindViewHolder(holder: BindHolder<ItemModel>, position: Int) {
        holder.bindHolder(items[position])
    }

    fun setOnItemClickListener(itemClick: ItemClickListener) {
        this.itemClick = itemClick
    }

    override fun getItemCount(): Int = items.size

    fun refillItem(girlList: List<Girl?>) {
        items.clear()
        appendItem(girlList)
    }

    fun appendItem(girlList: List<Girl?>) {
        girlList.filterNotNull().forEach {
            items.add(NormalItem(it))
        }
    }

    class DailyGirlHolder(parent: ViewGroup, private val call: ItemClickListener?) : BindHolder<NormalItem>(parent, R.layout.adapter_daily_girl) {

        override fun bindHolder(item: NormalItem) {
            val girl = item.girl
            Glide.with(itemView.context)
                    .load(girl.url)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(itemView.image)
            itemView.setOnClickListener { call?.onItemClick(girl, itemView) }
        }
    }

    class NormalItem(val girl: Girl) : ItemModel() {

        override fun getViewType(): Int {
            return 0
        }
    }

    interface ItemClickListener {
        fun onItemClick(girl: Girl, view: View)
    }
}
