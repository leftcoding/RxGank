package com.left.gank.ui.cure

import android.business.domain.Gift
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.left.gank.R
import com.left.gank.butterknife.adapter.BaseAdapter
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.item.ItemModel
import kotlinx.android.synthetic.main.adapter_daily_girl.view.*
import java.util.*

/**
 * Create by LingYan on 2016-07-05
 */
class CureAdapter internal constructor() : BaseAdapter<CureAdapter.NormalHolder<ItemModel>>() {
    private var callback: Callback? = null
    private val gifts = ArrayList<Gift>()
    private val items = ArrayList<CureItem>()

    override fun onCreateViewHolder(parent: ViewGroup, @ViewType.CureViewType viewType: Int): NormalHolder<ItemModel> {
        var viewHolder: NormalHolder<*>? = null
        if (viewType == ViewType.VIEW_TYPE_CURE) {
            viewHolder = CureHolder(parent, callback)
        }
        return (viewHolder as NormalHolder<ItemModel>?)!!
    }

    override fun onBindViewHolder(holder: NormalHolder<ItemModel>, position: Int) {
        holder.bindHolder(items[position])
    }

    fun setOnItemClickListener(callback: Callback) {
        this.callback = callback
    }

    override fun getItemViewType(position: Int): Int {
        return if (!items.isEmpty()) {
            items[position].viewType
        } else super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return gifts.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    internal fun refillItem(dailyGirlList: List<Gift>) {
        gifts.clear()
        appendItem(dailyGirlList)
    }

    fun appendItem(dailyGirlList: List<Gift>) {
        gifts.addAll(dailyGirlList)
        changeItems()
    }

    private fun changeItems() {
        for (gift in gifts) {
            items.add(CureItem(gift))
        }
    }

    internal class CureHolder(parent: ViewGroup, private val callback: Callback?) : NormalHolder<CureItem>(parent, R.layout.adapter_daily_girl) {
        override fun bindHolder(item: CureItem) {
            super.bindHolder(item)
            val gift = item.gift
            itemView.title!!.text = gift.title
            itemView.setOnClickListener { callback?.onClick(gift.url) }
        }
    }

    internal class CureItem(val gift: Gift) : ItemModel() {

        override fun getViewType(): Int {
            return ViewType.VIEW_TYPE_CURE
        }
    }

    interface Callback {
        fun onClick(url: String)
    }

    abstract class NormalHolder<TT : ItemModel>(parent: ViewGroup, layoutRes: Int) : BindHolder<TT>(parent, layoutRes) {

        override fun bindHolder(item: TT) {

        }
    }

    interface ViewType {

        companion object {
            const val VIEW_TYPE_CURE = 1
        }

        @IntDef(VIEW_TYPE_CURE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class CureViewType
    }
}
