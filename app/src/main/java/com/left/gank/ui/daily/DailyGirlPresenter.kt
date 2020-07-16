package com.left.gank.ui.daily

import android.business.domain.Gift
import android.business.domain.Girl
import android.content.Context
import android.jsoup.JsoupServer
import android.ui.logcat.Logcat
import com.uber.autodispose.ObservableSubscribeProxy
import org.jsoup.nodes.Document
import java.util.*

/**
 * Create by LingYan on 2016-10-26
 */

class DailyGirlPresenter(context: Context, view: DailyGirlContract.View) : DailyGirlContract.Presenter(context, view) {
    private var max: Int = 0
    private var pageMaxNumber: Int = 0

    override fun loadGirls(page: Int) {
        val finalPage = if (pageMaxNumber == 0) 1 else pageMaxNumber - page
        val url = getUrl(finalPage)
        JsoupServer.rxConnect(url)
                .build()
                .map { doc -> parseData(doc, url) }
                .doOnSubscribe {
                    if (view != null) {
                        view.showProgress()
                    }
                }
                .doFinally {
                    if (view != null) {
                        view.hideProgress()
                    }
                }
                .`as`<ObservableSubscribeProxy<List<Girl>>>(bindLifecycle<List<Girl>>())
                .subscribe({ list ->
                    if (view != null) {
                        view.loadDailyGirlSuccess(list, page)
                    }
                }, { e ->
                    Logcat.e(e)
                    if (view != null) {
                        view.loadDailyGirlFailure(e.toString())
                    }
                })
    }

    override fun getImages(url: String) {
        JsoupServer.rxConnect(url)
                .build()
                .map<String> { document ->
                    max = getImageUrlsMax(document)
                    getImageCountList(document)
                }
                .map<List<Gift>> { imageUrl -> parseImageUrl(imageUrl, url) }
                .doOnSubscribe {
                    if (view != null) {
                        view.showLoadingDialog()
                    }
                }
                .doFinally {
                    if (view != null) {
                        view.hideLoadingDialog()
                    }
                }
                .`as`<ObservableSubscribeProxy<List<Gift>>>(bindLifecycle<List<Gift>>())
                .subscribe({ imagesList ->
                    if (view != null) {
                        view.loadImagesSuccess(imagesList, url)
                    }
                }, { e ->
                    Logcat.e(e)
                    if (view != null) {
                        view.loadImagesFailure(e.toString())
                    }
                })
    }

    private fun parseImageUrl(imageUrl: String?, url: String?): List<Gift> {
        val imagesList = arrayListOf<Gift>()
        if (imageUrl != null) {
            var baseUrl: String? = null
            var name: String? = null
            var endType: String? = null
            val lastPointIndex: Int
            val lastNameIndex: Int
            if (imageUrl.contains(".")) {
                lastPointIndex = if (imageUrl.contains("-")) {
                    imageUrl.lastIndexOf("-")
                } else {
                    imageUrl.lastIndexOf(".")
                }
                lastNameIndex = imageUrl.lastIndexOf("/")
                baseUrl = imageUrl.substring(0, lastNameIndex)
                name = imageUrl.substring(lastNameIndex, lastPointIndex - 2)
                endType = imageUrl.substring(lastPointIndex, imageUrl.length)
            }

            var number: String
            var lastUrl: String
            var postUrl = url;
            for (i in 1..max) {
                number = if (i < 10) {
                    "0$i"
                } else {
                    i.toString()
                }
                lastUrl = baseUrl + name + number + endType
                postUrl = "$url/$i"
                imagesList.add(Gift(lastUrl, postUrl))
            }
        }
        return imagesList
    }

    /**
     * 筛选过滤得到月份集合
     */
    private fun parseData(doc: Document?, url: String): List<Girl> {
        val list = ArrayList<Girl>()
        doc?.also {
            if (pageMaxNumber == 0) {
                val pageUrls = doc.select(".pagenavi-cm .page-numbers")
                pageUrls?.isNotEmpty().also {
                    val pageUrl = pageUrls[0].attr("href")
                    pageUrl?.also {
                        val lastPageIndex = pageUrl.lastIndexOf("-")
                        val pageUrlStart = pageUrl.substring(0, lastPageIndex)
                        val pageUrlEnd = pageUrl.substring(lastPageIndex + 1)
                    }
                    pageMaxNumber = pageUrls[pageUrls.size - 2]?.text()?.toInt() ?: 0
                }
            }

            val imageUrls = doc.select(".comment-body img")
            imageUrls?.also {
                for (i in imageUrls.indices) {
                    list.add(Girl(imageUrls[i].attr("data-original"), url))
                }
            }
        }
        return list
    }

    private fun getImageUrlsMax(doc: Document?): Int {
        var max = 0
        doc?.apply {
            val page = doc.select(".prev-next-page")
            if (page.size > 0) {
                var mm = page[0].text()
                val split = mm.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val length = split.size
                if (length > 1) {
                    val s = split[1]
                    mm = s.substring(0, s.length - 1)
                    try {
                        max = Integer.parseInt(mm)
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }

                }
            }
        }
        return max
    }

    private fun getImageCountList(doc: Document?): String? {
        var imgUrl: String? = null
        if (doc != null) {
            val page = doc.select(".place-padding p a img")
            for (i in page.indices) {
                imgUrl = page[i].attr("src")
            }
        }
        return imgUrl
    }

    override fun onDestroy() {

    }

    companion object {
        private const val Girls_FIRST_URL = "https://www.mzitu.com/zipai/"
        fun getUrl(page: Int): String {
            return if (page == 1) Girls_FIRST_URL else "https://www.mzitu.com/zipai/comment-page-${page}/#comments"
        }
    }
}
