package com.left.gank.ui.discovered.video

import android.business.domain.Gank
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.left.gank.R
import kotlinx.android.synthetic.main.adapter_video.view.*

/**
 * 视频
 * Create by LingYan on 2016-04-25
 */
class VideoAdapter : RecyclerView.Adapter<VideoAdapter.GankViewHolder>() {
    private val results = mutableListOf<Gank>()
    private var onItemClickListener: Callback? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GankViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_video, parent, false)
        return GankViewHolder(view)
    }

    override fun onBindViewHolder(holder: GankViewHolder, position: Int) {
        val gank = results[position]
        holder.itemView.title.text = gank.desc
        holder.itemView.author.text = gank.who

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(gank)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun refillItems(getResults: List<Gank>) {
        val size = results.size
        results.clear()
        notifyItemRangeRemoved(0, size)
        appendItems(getResults)
    }

    fun appendItems(getResults: List<Gank>) {
        results.addAll(getResults)
        notifyItemRangeInserted(results.size, getResults.size)
    }

    fun setOnItemClickListener(onItemClickListener: Callback) {
        this.onItemClickListener = onItemClickListener
    }

    inner class GankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Callback {
        fun onClick(gank: Gank)
    }
}
