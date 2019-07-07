package com.left.gank.ui.cure

import android.business.domain.Gift
import android.content.Context
import android.jsoup.JsoupServer
import android.rxbus.RxApiManager
import android.text.TextUtils
import android.ui.logcat.Logcat
import com.left.gank.utils.CrashUtils
import com.left.gank.utils.StringUtils
import com.uber.autodispose.ObservableSubscribeProxy
import org.jsoup.nodes.Document
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Create by LingYan on 2016-10-26
 */

internal class CurePresenter(context: Context, view: CureContract.View) : CureContract.Presenter(context, view) {
    private val destroyFlag = AtomicBoolean(false)

    override fun loadData(page: Int) {
        fetchData(page, getUrl(page))
    }

    override fun loadImages(url: String) {
        val disposable = JsoupServer.rxConnect(url)
                .build()
                .doOnSubscribe {
                    if (isViewLife) {
                        view.showLoadingDialog()
                    }
                }
                .doFinally {
                    if (isViewLife) {
                        view.hideLoadingDialog()
                    }
                }
                .subscribe({ document ->
                    getDetailMaxPage(document)
                    val list = getImagesList(document)
                    if (isViewLife) {
                        view.loadImagesSuccess(list)
                    }
                }, { e ->
                    if (isViewLife) {
                        view.loadImagesFailure(errorTip)
                    }
                })
        RxApiManager.get().add(requestTag, disposable)
    }

    private fun fetchData(page: Int, url: String) {
        if (destroyFlag.get()) {
            return
        }

        val disposable = JsoupServer.rxConnect(url).build()
                .doOnSubscribe { dis ->
                    if (view != null) {
                        view.showProgress()
                    }
                }
                .doFinally {
                    if (view != null) {
                        view.hideProgress()
                    }
                }
                .`as`<ObservableSubscribeProxy<Document>>(bindLifecycle<Document>())
                .subscribe({ document ->
                    val maxPageNumber = getMaxPageNum(document)
                    val gifts = getPageLists(document)
                    if (gifts != null) {
                        if (view != null) {
                            view.loadDataSuccess(page, maxPageNumber, gifts)
                        }
                    }
                }, { Logcat.e(it) })
    }

    /**
     * 解析每面，封面入口地址
     */
    private fun getPageLists(doc: Document?): List<Gift>? {
        val list = ArrayList<Gift>()
        if (doc == null) {
            return null
        }
        val href = doc.select("#pins > li > a")
        val img = doc.select("#pins a img")
        val times = doc.select(".time")

        val countSize = href.size
        val imgSize = img.size
        val size = if (countSize > imgSize) imgSize else countSize

        if (size > 0) {
            for (i in 0 until size) {
                val imgUrl = img[i].attr("data-original")
                val title = img[i].attr("alt")
                val url = href[i].attr("href")
                val time = times[i].text()
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(Gift(imgUrl, url, time, title))
                }
            }
        }
        return list
    }

    private fun getMaxPageNum(doc: Document?): Int {
        val p = 0
        if (doc != null) {
            val count = doc.select(".nav-links a[href]")
            val size = count.size
            if (size > 0) {
                for (i in size - 1 downTo 0) {
                    val num = count[i].text()
                    if (StringUtils.isNumeric(num)) {
                        try {
                            return Integer.parseInt(num)
                        } catch (e: IllegalFormatException) {
                            Logcat.e(e)
                            CrashUtils.crashReport(e)
                        }

                    }
                }
            }
        }
        return p
    }

    private fun getDetailMaxPage(doc: Document?): Int {
        if (doc != null) {
            val pages = doc.select(".pagenavi span")
            if (pages != null && !pages.isEmpty()) {

            }
        }
        return 0
    }

    private fun getImagesList(doc: Document?): List<Gift> {
        val imagesList = ArrayList<Gift>()
        if (doc != null) {
            val images = doc.select(".main-image a img")
            val lengths = doc.select(".pagenavi a")
            var spanLength: String? = null
            if (lengths != null && lengths.size > 0) {
                val element = lengths[lengths.size - 2]
                if (element != null) {
                    spanLength = element.select("span")[0].html()
                }
            }
            var length: Int
            try {
                length = if (spanLength == null) -1 else Integer.valueOf(spanLength)
            } catch (e: IllegalFormatException) {
                length = -1
            }

            if (images != null && images.size > 0) {
                val src = images[0].attr("src")
                if (!TextUtils.isEmpty(src)) {
                    val index = src.lastIndexOf(".")
                    if (index > 0) {
                        var start = src.substring(0, index)
                        start = start.substring(0, start.length - 1)
                        val type = src.substring(index)
                        val sub = start.substring(0, start.length - 1)
                        if (length != -1) {
                            for (i in 1 until length) {
                                val page = if (i < 10) String.format(Locale.SIMPLIFIED_CHINESE, "0%d", i) else i.toString()
                                val url = sub + page + type
                                imagesList.add(Gift(url))
                            }
                        }
                    }
                }
            }
        }
        return imagesList
    }

    private fun getUrl(page: Int): String {
        return if (page == 1) BASE_URL else BASE_URL + "page/" + page
    }

    override fun destroy() {
        if (destroyFlag.compareAndSet(false, true)) {
            RxApiManager.get().remove(requestTag)
        }
        super.destroy()
    }

    override fun onDestroy() {

    }

    companion object {
        private val BASE_URL = "http://www.mzitu.com/xinggan/"
    }
}
