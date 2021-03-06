package com.left.gank.ui.gallery

import android.business.domain.Gift
import android.net.Uri
import android.os.Bundle
import android.ui.logcat.Logcat
import android.view.View
import com.left.gank.R
import com.left.gank.base.fragment.SupportFragment
import com.left.gank.rx.RxSchedulers
import com.left.gank.utils.RxSaveImage
import com.left.gank.utils.ShareUtils
import com.left.gank.widget.BottomSheetGalleryMenuDialog
import com.left.gank.widget.ProgressImageView
import com.uber.autodispose.ObservableSubscribeProxy
import kotlinx.android.synthetic.main.fragment_browse_picture.*

/**
 * 图片浏览
 * Create by LingYan on 2016-12-19
 */
class GalleryFragment : SupportFragment() {
    private var url: String? = null
    private var postUrl: String? = null

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_browse_picture
    }

    private val progressCallback = object : ProgressImageView.ImageCallback {
        override fun onImageClick(v: View) {

        }

        override fun onLongClick(v: View) {
            BottomSheetGalleryMenuDialog(activity!!).apply {
                setCallback(callback)
                show()
            }
        }
    }

    private val callback = object : BottomSheetGalleryMenuDialog.Callback {
        override fun share() {
            onEvent(false)
        }

        override fun save() {
            onEvent(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            url = getString(IMAGE_URL)
            postUrl = getString(IMAGE_POST_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_image!!.apply {
            load(url, postUrl, context)
            setImageViewOnClick(progressCallback)
        }
    }

    private fun onEvent(save: Boolean) {
        RxSaveImage.convertBitmap(context, url)
                .observeOn(RxSchedulers.mainThread())
                .map<Uri> { bitmap ->
                    val file = RxSaveImage.createImageFile(bitmap.hashCode().toString())
                    if (file != null) {
                        return@map RxSaveImage.bitmapToImage(context, bitmap, file)
                    }
                    null
                }
                .`as`<ObservableSubscribeProxy<Uri>>(bindLifecycle<Uri>())
                .subscribe({ uri -> if (save) shortToast(uri.toString()) else ShareUtils.shareSingleImage(context!!, uri) }, { throwable -> Logcat.e(throwable) })

    }

    companion object {
        const val IMAGE_URL = "Image_Url"
        const val IMAGE_POST_URL = "Post_Url"

        fun newInstance(gift: Gift): GalleryFragment = GalleryFragment().apply {
            arguments = Bundle().apply {
                putString(IMAGE_URL, gift.imgUrl)
                putString(IMAGE_POST_URL, gift.url)
            }
        }
    }
}
