package com.left.gank.ui.pure

import android.business.domain.Gift
import android.content.Context
import android.jsoup.JsoupServer
import android.text.TextUtils
import android.ui.logcat.Logcat
import com.left.gank.utils.CrashUtils
import com.left.gank.utils.ListUtils
import com.left.gank.utils.StringUtils
import com.socks.library.KLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jsoup.nodes.Document
import java.util.*

/**
 * Create by LingYan on 2016-12-27
 */

class PurePresenter internal constructor(context: Context, view: PureContract.View) : PureContract.Presenter(context, view) {
    private var maxPageNumber: Int = 0
    private var maxImages: Int = 0

    override fun loadData(page: Int) {
        val url = getUrl(page)
        JsoupServer.rxConnect(url).build()
                .doOnSubscribe {
                    if (view != null) {
                        view.showProgress()
                    }
                }
                .doFinally { view.hideProgress() }
                .subscribe(object : Observer<Document> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(document: Document) {
                        maxPageNumber = getMaxPageNum(document)
                        val gifts = getPageLists(document)
                        if (gifts != null) {
                            if (view != null) {
                                view.loadDataSuccess(page, gifts)
                            }
                            return
                        }
                        if (view != null) {
                            view.loadDataFailure(page, errorTip)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Logcat.e(e)
                        if (view != null) {
                            view.loadDataFailure(page, errorTip)
                        }
                    }

                    override fun onComplete() {}
                })
    }

    override fun loadImages(url: String) {
        JsoupServer.rxConnect(url)
                .build()
                .doOnSubscribe {
                    if (view != null) {
                        view.showLoadingDialog()
                    }
                }
                .map { document ->
                    maxImages = getImageMaxPage(document)
                    val firstUrl = getImageFirstUrl(document)
                    getImages(firstUrl, url)
                }
                .doFinally {
                    if (view != null) {
                        view.hideLoadingDialog()
                    }
                }
                .subscribe(object : Observer<List<Gift>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(list: List<Gift>) {
                        if (ListUtils.isNotEmpty(list)) {
                            if (isViewLife) {
                                view.loadImagesSuccess(list)
                                return
                            }
                        }
                        if (isViewLife) {
                            view.loadImagesFailure(errorTip)
                        }
                    }

                    override fun onError(e: Throwable) {
                        KLog.e(e)
                        CrashUtils.crashReport(e)
                    }

                    override fun onComplete() {}
                })
    }

    private fun getImageMaxPage(doc: Document?): Int {
        var max = 0
        if (doc != null) {
            val pages = doc.select(".pagenavi a[href]")
            val size = pages.size
            for (i in size - 1 downTo 1) {
                val page = pages[i].text()
                if (StringUtils.isNumeric(page)) {
                    max = Integer.parseInt(page)
                    break
                }
            }
        }
        return max
    }

    private fun getImageFirstUrl(doc: Document): String {
        val links = doc.select(".main-image img[src$=.jpg]")
        return links[0].attr("src")
    }

    private fun getImages(url: String, postUrl: String): ArrayList<Gift> {
        val imagesList = ArrayList<Gift>()
        var baseUrl: String? = null
        var name: String? = null
        var endType: String? = null
        val lastPointIndex: Int
        val lastNameIndex: Int
        if (url.contains(".")) {
            lastPointIndex = if (url.contains("-")) {
                url.lastIndexOf("-")
            } else {
                url.lastIndexOf(".")
            }
            lastNameIndex = url.lastIndexOf("/")
            baseUrl = url.substring(0, lastNameIndex)
            name = url.substring(lastNameIndex, lastPointIndex - 2)
            endType = url.substring(lastPointIndex)
        }

        var number: String
        var lastUrl: String
        for (i in 1..maxPageNumber) {
            number = if (i < 10) {
                "0$i"
            } else {
                i.toString()
            }
            lastUrl = baseUrl + name + number + endType
            imagesList.add(Gift(lastUrl, postUrl))
        }
        return imagesList
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

    private fun getUrl(page: Int): String {
        return if (page == 1) {
            BASE_URL
        } else {
            nextUrl + page
        }
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
                            KLog.e(e)
                            CrashUtils.crashReport(e)
                        }

                    }
                }
            }
        }
        return p
    }

    override fun onDestroy() {

    }

    companion object {
        private const val BASE_URL = "http://www.mzitu.com/mm/"
        private const val HOST = "page/"
        private const val nextUrl = BASE_URL + HOST
    }
}
