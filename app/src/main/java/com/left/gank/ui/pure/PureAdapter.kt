package com.left.gank.ui.pure

import android.business.domain.Gift
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.left.gank.R
import com.left.gank.butterknife.adapter.BaseAdapter
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.item.ItemModel
import com.left.gank.utils.ListUtils
import kotlinx.android.synthetic.main.adapter_gift.view.*
import java.util.*

/**
 * Create by LingYan on 2016-04-25
 */
class PureAdapter : BaseAdapter<BindHolder<*>>() {
    private val gifts = ArrayList<Gift>()
    private val items = ArrayList<ItemModel>()
    private var callback: Callback? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            items.clear()
            ListUtils.isNotEmpty(gifts).let {
                gifts.forEach {
                    items.add(GiftItem(it))
                }
            }
        }
    }

    init {
        registerAdapterDataObserver(observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<*> {
        when (viewType) {
            Type.IMAGE -> {
                return GiftHolder(parent, callback)
            }
            else -> {
                throw RuntimeException("invalid viewType = $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: BindHolder<*>, position: Int) {
        when (holder) {
            is GiftHolder -> {
                holder.bindHolder(items[position] as GiftItem)
            }
            else -> {
                throw RuntimeException("invalid holder = $holder")
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (!items.isEmpty()) {
            items[position].viewType
        } else super.getItemViewType(position)
    }

    internal fun refillItems(getResults: List<Gift>) {
        gifts.clear()
        items.clear()
        appendItems(getResults)
    }

    fun appendItems(getResults: List<Gift>) {
        gifts.addAll(getResults)
    }

    fun clear() {
        gifts.clear()
    }

    fun setOnItemClickListener(callback: Callback) {
        this.callback = callback
    }

    internal class GiftHolder(parent: ViewGroup, private val callback: Callback?) : BindHolder<GiftItem>(parent, R.layout.adapter_gift) {

        override fun bindHolder(item: GiftItem) {
            val gift = item.gift
            val context = itemView.context
            itemView.title!!.text = gift.title
            itemView.author!!.text = gift.time

            val glideUrl = GlideUrl(gift.imgUrl, LazyHeaders.Builder()
                    .addHeader("referer", "http://www.mzitu.com/mm/")
                    .build())

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .apply(RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(itemView.goods_background!!)

            itemView.setOnClickListener {
                callback?.onItemClick(gift)
            }
        }
    }

    internal class GiftItem(val gift: Gift) : ItemModel() {
        override fun getViewType(): Int {
            return Type.IMAGE
        }
    }

    interface Type {
        companion object {
            const val IMAGE = 0
        }
    }

    interface Callback {
        fun onItemClick(gift: Gift)
    }
}
