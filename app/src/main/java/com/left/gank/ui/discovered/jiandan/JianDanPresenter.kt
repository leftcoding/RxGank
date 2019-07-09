package com.left.gank.ui.discovered.jiandan

import android.content.Context
import android.jsoup.JsoupServer
import com.left.gank.domain.JianDanBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jsoup.nodes.Document
import java.util.*


/**
 * Create by LingYan on 2016-11-21
 */

class JianDanPresenter(context: Context, view: JiandanContract.View) : JiandanContract.Presenter(context, view) {

    private fun mapResult(doc: Document?): List<JianDanBean> {
        val list = ArrayList<JianDanBean>()
        if (doc != null) {
            val herfs = doc.select(".thumb_s a")
            val imgs = doc.select(".thumb_s a img")
            val types = doc.select(".indexs")
            val titles = doc.select(".thetitle")

            var url: String
            var title: String
            var type: String
            var imgUrl: String?
            for (i in herfs.indices) {
                url = herfs[i].attr("href")
                title = titles[i].text()
                type = types[i].text()
                imgUrl = imgs[i].attr("data-original")
                if (imgUrl != null && imgUrl.startsWith("//")) {
                    imgUrl = imgUrl.substring(2, imgUrl.length)
                    imgUrl = "http://$imgUrl"
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
        private val BASE_URL = "http://i.jandan.net/page/"
        private val LIMIT = 24
    }
}
