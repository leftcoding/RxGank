package com.left.gank.ui.daily

import android.business.domain.Gift
import android.business.domain.Girl
import android.content.Context
import android.jsoup.JsoupServer
import android.ui.logcat.Logcat
import com.uber.autodispose.ObservableSubscribeProxy
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jsoup.nodes.Document
import java.util.*

/**
 * Create by LingYan on 2016-10-26
 */

class DailyGirlPresenter(context: Context, view: DailyGirlContract.View) : DailyGirlContract.Presenter(context, view) {
    private var imagesList: ArrayList<Gift>? = null
    private var max: Int = 0

    override fun loadGirls() {
        JsoupServer.rxConnect(MEIZI_FIRST_URL)
                .build()
                .map { doc -> parseData(doc) }
                .`as`<ObservableSubscribeProxy<List<Girl>>>(bindLifecycle<List<Girl>>())
                .subscribe({ list ->
                    if (view != null) {
                        view.loadDailyGirlSuccess(list)
                    }
                }, { e -> Logcat.e(e) })
    }

    private fun getImages(url: String) {
        JsoupServer.rxConnect(url).build()
                .map<String> { document -> getImageCountList(document) }
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {
                        //empty
                    }

                    override fun onNext(url: String) {
                        imagesList = ArrayList()
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
                            endType = url.substring(lastPointIndex, url.length)
                        }

                        var number: String
                        var lastUrl: String
                        for (i in 1..max) {
                            number = if (i < 10) {
                                "0$i"
                            } else {
                                i.toString()
                            }
                            lastUrl = baseUrl + name + number + endType
                            imagesList!!.add(Gift(lastUrl))
                        }
                    }
                })
    }

    /**
     * 筛选过滤得到月份集合
     */
    private fun parseData(doc: Document?): List<Girl> {
        val list = ArrayList<Girl>()
        if (doc != null) {
            val times = doc.select(".post-content .archive-brick")
            val href = doc.select(".post-content .archive-brick a")
            for (i in href.indices) {
                list.add(Girl(href[i].attr("href"), times[i].text()))
            }
        }
        return list
    }


    private fun getImageUrlsMax(doc: Document?): Int {
        var max = 0
        if (doc != null) {
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

    private fun getImageUrl(url: String): String {
        return "$url/0"
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
        private const val MEIZI_FIRST_URL = "http://m.mzitu.com/all"
    }
}
