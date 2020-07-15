package com.left.gank.ui.gallery

import android.app.WallpaperManager
import android.business.domain.Gift
import android.business.domain.Solid
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.ui.logcat.Logcat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.left.gank.R
import com.left.gank.base.activity.SupportActivity
import com.left.gank.utils.*
import com.left.gank.widget.WheelView
import com.socks.library.KLog
import com.uber.autodispose.ObservableSubscribeProxy
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_browse_picture.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * 相册
 * Create by LingYan on 2016-4-25
 */
class GalleryActivity : SupportActivity() {
    private var pagerAdapter: GalleryPagerAdapter? = null
    private var wallpaperManager: WallpaperManager? = null

    private var gifts: MutableList<Gift>? = null
    private var bitmap: Bitmap? = null
    private var isCanPlay = true
    private var position: Int = 0
    private var disposable: Disposable? = null
    private var url: String? = null

    override fun getContentId(): Int {
        return R.layout.activity_browse_picture
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {

        }

        override fun onPageSelected(_position: Int) {
            position = _position
            setupPageNumber(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                stopBrowse()
            }
        }
    }

    private val selectList: ArrayList<String>?
        get() {
            val list: ArrayList<String> = ArrayList()
            if (!ListUtils.isEmpty(gifts)) {
                for (i in 0 until giftSize) {
                    list.add(i, (i + 1).toString())
                }
            }
            return list
        }

    private val giftSize: Int
        get() = if (gifts == null) 0 else gifts!!.size

    private val adapterCount: Int
        get() = if (pagerAdapter == null) 0 else pagerAdapter!!.count

    private val isPositionEnd: Boolean
        get() = adapterCount == position + 1

    private val imageUrl: String
        get() {
            val position = view_pager!!.currentItem
            return gifts!![position].imgUrl
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseBundle()
        setupPageNumber(position)

        pagerAdapter = GalleryPagerAdapter(supportFragmentManager)
        gifts?.let {
            pagerAdapter!!.setGifts(it, url)
        }
        view_pager!!.let {
            it.adapter = pagerAdapter
            it.currentItem = position
            it.offscreenPageLimit = PAGE_LIMIT
            it.addOnPageChangeListener(onPageChangeListener)
            pagerAdapter!!.notifyDataSetChanged()
        }
        browse_auto.setOnClickListener {
            changeBrowse()
        }
        progress_page.setOnClickListener {
            stopBrowse()
            val list = selectList
            if (list != null) {
                val outerView = View.inflate(this, R.layout.view_wheel, null)
                val wv = outerView.findViewById<WheelView>(R.id.wheel_view_wv)
                wv.setItems(list)
                wv.setSeletion(position)
                wv.onWheelViewListener = object : WheelView.OnWheelViewListener() {
                    override fun onSelected(selectedIndex: Int, item: String) {
                        position = selectedIndex - 1
                    }
                }

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.gallery_page_select)
                builder.setView(outerView)
                builder.setPositiveButton(R.string.dialog_ok) { dialog, which ->
                    dialog.dismiss()
                    view_pager!!.currentItem = position
                }
                builder.show()
            }
        }
    }

    private fun setupPageNumber(position: Int) {
        val size = if (gifts == null) 0 else gifts!!.size
        val giftSize = size.toString()
        val index = (position + 1).toString()
        progress_page!!.text = StringHtml.getStringSize(index, giftSize, getString(R.string.page_format_mark), NUMBER_COLOR, 22)
    }

    private fun parseBundle() {
        intent.extras?.let {
            gifts = intent.getParcelableArrayListExtra(EXTRA_LIST)
            url = intent.getStringExtra(EXTRA_URL)
        }
    }

    private fun changeImageList(resultsBeen: List<Solid>): List<Gift> {
        val list = ArrayList<Gift>()
        if (!ListUtils.isEmpty(resultsBeen)) {
            var resultsBean: Solid
            var url: String
            for (i in resultsBeen.indices) {
                resultsBean = resultsBeen[i]
                url = resultsBean.url
                list.add(Gift(url))
            }
        }
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.meizi_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.meizi_save -> saveImagePath(imageUrl, false)
            R.id.meizi_share -> saveImagePath(imageUrl, true)
            R.id.meizi_wallpaper -> makeWallpaperDialog()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeBrowse() {
        if (isCanPlay) {
            isCanPlay = false

            if (!isPositionEnd) {
                browse_auto!!.setImageDrawable(resources.getDrawable(R.drawable.ic_gallery_stop, theme))
                timerBrowse()
            } else {
                stopBrowse()
            }
        } else {
            stopBrowse()
        }
    }

    private fun stopBrowse() {
        disposable?.apply {
            dispose()
        }
        isCanPlay = true
        browse_auto!!.setImageDrawable(resources.getDrawable(R.drawable.ic_gallery_play))
    }

    private fun timerBrowse() {
        disposable = Observable.interval(INITIAL_DELAY.toLong(), INTERVALS.toLong(), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .`as`<ObservableSubscribeProxy<Long>>(bindLifecycle())
                .subscribe({ long ->
                    if (long >= adapterCount) {
                        stopBrowse()
                    } else {
                        val next = position + 1
                        view_pager!!.setCurrentItem(next, true)
                    }
                    if (isPositionEnd) {
                        stopBrowse()
                    }
                }, { t -> Logcat.e(t) })
    }

    private fun makeWallpaperDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.meizi_is_wallpaper)
        builder.setNegativeButton(R.string.dialog_cancel) { dialog, which -> dialog.cancel() }
        builder.setPositiveButton(R.string.dialog_ok) { dialog, which -> setWallPaper(imageUrl) }
        builder.create().show()
    }

    private fun setWallPaper(url: String) {
        RxSaveImage.convertBitmap(this, url)
                .observeOn(AndroidSchedulers.mainThread())
                .`as`<ObservableSubscribeProxy<Bitmap>>(bindLifecycle())
                .subscribe(object : Observer<Bitmap> {
                    override fun onError(e: Throwable) {
                        ToastUtils.showToast(baseContext, R.string.meizi_wallpaper_failure)
                        KLog.e(e)
                    }

                    override fun onComplete() {
                        revokeWallpaper()
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(bitmap: Bitmap) {
                        wallpaperManager = WallpaperManager
                                .getInstance(applicationContext)
                        try {
                            val wallpaperDrawable = wallpaperManager!!.drawable
                            this@GalleryActivity.bitmap = (wallpaperDrawable as BitmapDrawable).bitmap
                            wallpaperManager!!.setBitmap(bitmap)
                        } catch (e: IOException) {
                            KLog.e(e)
                        }
                    }
                })
    }

    private fun revokeWallpaper() {
        Snackbar.make(view_pager!!, R.string.meizi_wallpaper_success, Snackbar.LENGTH_LONG)
                .setAction(R.string.revoke) { v ->
                    try {
                        if (bitmap != null) {
                            wallpaperManager!!.setBitmap(bitmap)
                        }
                    } catch (e: IOException) {
                        KLog.e(e)
                    } finally {
                        bitmap = null
                    }
                    ToastUtils.showToast(baseContext, R.string.meizi_revoke_success)
                }
                .show()
    }

    private fun saveImagePath(imgUrl: String, isShare: Boolean) {
        RxSaveImage.convertBitmap(this, imgUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .map<Uri> { bitmap ->
                    val file = RxSaveImage.createImageFile(bitmap.hashCode().toString())
                    if (file != null) {
                        return@map RxSaveImage.bitmapToImage(baseContext, bitmap, file)

                    }
                    null
                }
                .filter { true }
                .`as`<ObservableSubscribeProxy<Uri>>(bindLifecycle<Uri>())
                .subscribe({ uri ->
                    if (isShare) {
                        ShareUtils.shareSingleImage(baseContext, uri)
                    } else {
                        val msg = String.format(getString(R.string.meizi_picture_save_path), uri.toString())
                        ToastUtils.showToast(baseContext, msg)
                    }
                }, { throwable -> Logcat.e(throwable) })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (gifts != null) {
            gifts!!.clear()
            gifts = null
        }
    }

    companion object {
        const val FILE_PATH = "GankLy_pic"
        const val INTERVALS = 3000
        const val INITIAL_DELAY = 1000
        const val PAGE_LIMIT = 5

        const val TYPE_INDEX = 1
        const val TYPE = "Type"
        const val EXTRA_GANK = "Gank"
        const val EXTRA_GIFT = "Gift"
        const val EXTRA_DAILY = "Daily"
        const val EXTRA_MODEL = "Model"
        const val EXTRA_POSITION = "Position"
        const val EXTRA_LIST = "Extra_List"
        const val EXTRA_URL = "Extra_Url"
        private const val NUMBER_COLOR = "#8b0000"
    }
}
