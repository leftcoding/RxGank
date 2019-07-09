package com.left.gank.ui.discovered.jiandan

import android.content.Context
import android.jsoup.JsoupServer
import android.ui.logcat.Logcat
import com.left.gank.domain.JianDanBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jsoup.nodes.Document


/**
 * Create by LingYan on 2016-11-21
 */

class JiandanPresenter(context: Context, view: JiandanContract.View) : JiandanContract.Presenter(context, view) {

    private fun mapResult(doc: Document?): List<JianDanBean> {
        Logcat.d(doc)
        val list = mutableListOf<JianDanBean>()
        if (doc != null) {
            var herfs = doc.select(".thumb_s a")
            val imgs = doc.select(".thumb_s a img")
            val types = doc.select(".indexs")
            val titles = doc.select(".thetitle")

            if (herfs == null || herfs.isEmpty()) {
                herfs = doc.select(".post a")
            }

            val imagesEmpty = imgs == null || imgs.isEmpty()
            var url: String
            var title: String
            var type: String
            var imgUrl: String? = null
            for (i in herfs.indices) {
                url = herfs[i].attr("href")
                title = titles[i].text()
                type = types[i].text()
                if (!imagesEmpty) {
                    imgUrl = imgs[i].attr("data-original")
                }
                imgUrl?.apply {
                    if (startsWith("//")) {
                        substring(2, length)
                        imgUrl = "http://$imgUrl"
                    }
                }

                list.add(JianDanBean(url, title, type, imgUrl))
            }
        }
        return list
    }

    override fun loadJianDan(page: Int) {
        val url = BASE_URL + page
        JsoupServer.rxConnect(url).build()
                .map {
                    mapResult(it)
                }
                .doOnSubscribe { if (view != null) view.showProgress() }
                .doFinally { if (view != null) view.hideProgress() }
                .subscribe(object : Observer<List<JianDanBean>> {
                    override fun onError(e: Throwable) {
                        if (view != null) {
                            view.loadJiandanFailure(page, e.toString())
                        }
                    }

                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(list: List<JianDanBean>) {
                        if (view != null) {
                            view.loadJiandanSuccess(page, list)
                        }
                    }
                })
    }

    override fun onDestroy() {

    }

    companion object {
        private const val BASE_URL = "http://i.jandan.net/page/"
    }
}
