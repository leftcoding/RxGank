package com.left.gank.ui.discovered.jiandan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.left.gank.R
import com.left.gank.domain.JianDanBean
import kotlinx.android.synthetic.main.adapter_jiandan.view.*

/**
 * Create by LingYan on 2016-07-20
 */
class JiandanAdapter internal constructor() : RecyclerView.Adapter<JiandanAdapter.JiandanHolder>() {
    private val jiandans = mutableListOf<JianDanBean>()
    private var itemClick: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JiandanHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_jiandan, parent, false)
        return JiandanHolder(view)
    }

    override fun onBindViewHolder(holder: JiandanHolder, position: Int) {
        val result = jiandans[position]
        holder.itemView.jiandan_txt_title.text = result.title
        holder.itemView.jiandan_txt_author.text = result.type

        Glide.with(holder.itemView.context!!)
                .load(result.imgUrl)
                .into(holder.itemView.jiandan_img)

        holder.itemView.setOnClickListener {
            itemClick?.onClick(result)
        }
    }

    fun setListener(itemClick: Callback) {
        this.itemClick = itemClick
    }

    internal fun updateItem(list: List<JianDanBean>) {
        jiandans.clear()
        appendItem(list)
    }

    fun appendItem(list: List<JianDanBean>) {
        jiandans.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return jiandans.size
    }

    inner class JiandanHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Callback {
        fun onClick(result: JianDanBean)
    }
}
