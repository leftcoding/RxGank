package com.left.gank.ui.ios.text

import android.view.ViewGroup
import com.left.gank.R
import com.left.gank.butterknife.holder.BindHolder
import kotlinx.android.synthetic.main.adapter_ios.view.*

class TextHolder(parent: ViewGroup, private val itemCallBack: ItemCallback?) : BindHolder<TextModel>(parent, R.layout.adapter_ios) {
    override fun bindHolder(item: TextModel) {
        val gank = item.gank
        itemView.time!!.text = item.time
        itemView.title!!.text = gank.desc
        itemView.author_name!!.text = gank.who

        itemView.setOnClickListener { v ->
            itemCallBack?.onItemClick(v, gank)
        }
    }
}