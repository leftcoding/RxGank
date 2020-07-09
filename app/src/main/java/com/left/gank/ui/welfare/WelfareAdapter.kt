package com.left.gank.ui.welfare

import android.business.domain.Gank
import android.content.Context
import android.graphics.Bitmap
import android.util.ArrayMap
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.left.gank.R
import com.left.gank.butterknife.adapter.BindHolder
import com.left.gank.butterknife.adapter.FootAdapter
import com.left.gank.butterknife.item.ItemModel
import kotlinx.android.synthetic.main.adapter_meizi.view.*
import java.util.*

/**
 * Create by LingYan on 2018-09-25
 */
class WelfareAdapter internal constructor(context: Context, lifecycleOwner: LifecycleOwner)
    : FootAdapter<WelfareAdapter.ViewHolder, List<Gank>>(context, lifecycleOwner) {
    private val models = ArrayList<ItemModel>()
    private var itemClickListener: ItemClickListener? = null

    init {
        setFootModel(false)
    }

    fun setListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun rxCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent, itemClickListener, heights)
    }

    override fun addItems(): List<ItemModel> {
        return models
    }

    override fun fillItems(list: List<Gank>) {
        models.clear()
        appendItems(list)
    }

    override fun appendItems(list: List<Gank>) {
        for (gank in list) {
            models.add(ImageItem(gank))
        }
    }

    class ViewHolder(parent: ViewGroup, private val itemClickListener: ItemClickListener?, private val heights: ArrayMap<String, Int>) : BindHolder<ImageItem>(parent, R.layout.adapter_meizi) {
        private val context: Context = parent.context

        override fun bindHolder(item: ImageItem) {
            val gank = item.gank
            val url = gank.url

            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : BitmapImageViewTarget(itemView.image) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val height = resource.height
                            val width = resource.width
                            val imageWidth = itemView.image.width
                            //                            Logcat.d(">>height:" + height + " width:" + width + " imageWidth:" + imageWidth);

                            var finalHeight = -1
                            if (heights.containsKey(url)) {
                                val i = heights[url]
                                if (i != null) {
                                    finalHeight = i
                                }
                            }

                            if (finalHeight == -1) {
                                finalHeight = height * imageWidth / width
                                heights[url] = finalHeight
                            }

                            setCardViewLayoutParams(imageWidth, finalHeight)
                            super.onResourceReady(resource, transition)
                        }
                    })

            itemView.setOnClickListener {
                itemClickListener?.onItem(itemView.image, url)
            }
        }

        private fun setCardViewLayoutParams(width: Int, height: Int) {
            val layoutParams = itemView.group_item!!.layoutParams
            layoutParams.width = width
            layoutParams.height = height
            itemView.group_item!!.layoutParams = layoutParams
        }
    }

    class ImageItem internal constructor(val gank: Gank) : ItemModel() {
        override fun getViewType(): Int {
            return 0
        }
    }

    interface ItemClickListener {
        fun onItem(view: View, url: String)
    }

    companion object {
        private val heights = ArrayMap<String, Int>()
    }
}
