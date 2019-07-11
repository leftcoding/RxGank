package com.left.gank.ui.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.data.entity.UrlCollect
import com.left.gank.ui.base.activity.SupportActivity
import com.left.gank.utils.AppUtils
import com.left.gank.utils.ShareUtils
import com.left.gank.utils.ToastUtils
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.*
import com.uber.autodispose.ObservableSubscribeProxy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_web.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

/**
 * Create by LingYan on 2016-5-10
 */
class JiandanWebActivity : SupportActivity() {
    private var cusWebView: WebView? = null

    private var url: String? = null
    private var title: String? = null
    private var type: String? = null
    private var author: String? = null
    private var isCollect: Boolean = false
    private val isInitCollect: Boolean = false
    private var states = CollectStates.NORMAL
    private var fromWay: Int = 0
    private var history: String? = null
    private val strings = ArrayList<String>()

    private val loadDataBaseUrl: String?
        get() {
            if (!TextUtils.isEmpty(url)) {
                if (isJianDanUrl) {
                    return "http://i.jandan.net"
                } else if (isPmUrl) {
                    return "http://woshipm.com"
                }
            }
            return url
        }

    private val pmRemoveDivs: List<String>
        get() {
            val list = ArrayList<String>()
            list.add(".downapp")
            list.add(".footer")
            list.add(".metabar")
            return list
        }

    private val jianDanRemoveDivs: List<String>
        get() {
            val list = ArrayList<String>()
            list.add("#headerwrapper")
            list.add("#footer")
            list.add("#commentform")
            list.add(".comment-hide")
            list.add(".share-links")
            list.add(".star-rating")
            list.add(".s_related")
            list.add(".jandan-zan")
            return list
        }

    private val isJianDanUrl: Boolean
        get() = url!!.contains("jandan.net") || url!!.contains("i.jandan.net")

    private val isPmUrl: Boolean
        get() = url!!.contains("woshipm.com")

    internal enum class CollectStates {
        NORMAL, COLLECT, UN_COLLECT
    }

    override fun getContentId(): Int {
        return R.layout.activity_web
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = title
        setSupportActionBar(toolbar)
        val bar = supportActionBar
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { v -> onBackPressed() }

        cusWebView = WebView(this, null)

        web_view!!.addView(cusWebView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT))

        val settings = cusWebView!!.settings
        cusWebView!!.requestFocusFromTouch() //支持获取手势焦点，输入用户名、密码或其他
        settings.javaScriptEnabled = true  //支持js
        settings.domStorageEnabled = true //
        settings.setSupportZoom(true) //设置支持缩放
        settings.builtInZoomControls = true //
        settings.displayZoomControls = false//是否显示缩放控件
        settings.useWideViewPort = true  //将图片调整到适合webview的大小
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //支持内容重新布局
        settings.supportMultipleWindows()  //多窗口
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK  //关闭webview中缓存
        settings.allowFileAccess = true  //设置可以访问文件
        settings.setNeedInitialFocus(true) //当webview调用requestFocus时为webview设置节点
        settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        settings.loadsImagesAutomatically = true  //支持自动加载图片
        settings.defaultTextEncodingName = "utf-8"//设置编码格式
        cusWebView!!.setWebViewClient(MyWebViewClient())
        cusWebView!!.setWebChromeClient(MyWebChromeClient())

        val bundle = intent.extras
        if (bundle != null) {
            url = bundle.getString(URL)
            title = bundle.getString(TITLE)
            type = bundle.getString(TYPE, Constants.JIANDAN)
            author = bundle.getString(AUTHOR)
            fromWay = bundle.getInt(FROM_WAY)
        }

        if (!TextUtils.isEmpty(url)) {
            parseLoadUrlData(filterUrl(url!!))
        }
    }

    private fun filterUrl(url: String): String {
        if (url.contains("/") && url.startsWith("http://jandan.net")) {
            return url.replace("jandan.net", "i.jandan.net")
        }
        return url
    }

    private fun parseLoadUrlData(url: String) {
        Observable.create<String> { subscriber ->
            try {
                var doc = Jsoup.connect(url)
                        .userAgent(USERAGENT)
                        .timeout(timeout)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get()
                var docHtml: String? = null
                if (doc != null) {
                    doc = removeDivs(doc)
                    docHtml = doc.html()
                }
                if (!TextUtils.isEmpty(docHtml)) {
                    subscriber.onNext(docHtml!!)
                    subscriber.onComplete()
                } else {
                    subscriber.onError(Throwable("doc html is null"))
                }
            } catch (e: IOException) {
                subscriber.onError(e)
            }
        }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`<ObservableSubscribeProxy<String>>(bindLifecycle())
                .subscribe { s ->
                    if (TextUtils.isEmpty(s)) {
                        cusWebView!!.loadUrl(url)
                    } else {
                        cusWebView!!.loadDataWithBaseURL(loadDataBaseUrl, s, "text/html", "utf-8", this.url)
                    }
                }
    }

    private fun removeDivs(doc: Document): Document {
        var doc = doc
        var list: List<String> = ArrayList()
        if (isJianDanUrl) {
            list = jianDanRemoveDivs
        } else if (isPmUrl) {
            list = pmRemoveDivs
        }

        if (list.size != 0) {
            for (i in list.indices) {
                doc.select(list[i]).remove()
            }
        }

        if (isJianDanUrl) {
            doc = removePrevDiv(doc)
            doc = removeScripts(doc)
        }
        return doc
    }

    private fun removePrevDiv(doc: Document): Document {
        val elements = doc.select(".entry")
        if (elements != null && elements.size > 1) {
            elements[1].remove()
        }
        return doc
    }

    private fun removeScripts(doc: Document): Document {
        val sc = doc.select("script")
        for (i in sc.indices) {
            val scText = sc[i].toString()
            if (!TextUtils.isEmpty(scText) && scText.contains("decodeURIComponent") &&
                    scText.contains("s_related")) {
                sc[i].remove()
            }
        }
        return doc
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        switchCollectIcon(menu.findItem(R.id.welfare_collect))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.welfare_collect -> {
                states = CollectStates.NORMAL
                if (!isCollect) {
                    if (!isInitCollect) {
                        states = CollectStates.COLLECT
                    }
                    Snackbar.make(web_main!!, R.string.collect_success, Snackbar.LENGTH_SHORT).show()
                } else {
                    if (isInitCollect) {
                        states = CollectStates.UN_COLLECT
                    }
                    Snackbar.make(web_main!!, R.string.collect_cancel, Snackbar.LENGTH_SHORT).show()
                }

                isCollect = !isCollect
                switchCollectIcon(item)
                return true
            }
            R.id.welfare_share -> {
                ShareUtils.getInstance().shareText(this, cusWebView!!.title, url)
                return true
            }
            R.id.welfare_copy_url -> {
                AppUtils.copyText(this, url)
                ToastUtils.showToast(baseContext, R.string.tip_copy_success)
                return true
            }
            R.id.welfare_refresh -> {
                cusWebView!!.reload()
                return true
            }
            R.id.welfare_browser -> {
                openBrowser(url)
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchCollectIcon(item: MenuItem) {
        if (isCollect) {
            item.setIcon(R.drawable.navigation_collect_prs)
        } else {
            item.setIcon(R.drawable.navigation_collect_nor)
        }
    }

    private fun openBrowser(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        //查询是否有符合的Activity
        if (intent.resolveActivity(this@JiandanWebActivity.packageManager) != null) {
            this@JiandanWebActivity.startActivity(intent)
        } else {
            ToastUtils.showToast(baseContext, R.string.web_open_failed)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cusWebView != null && cusWebView!!.canGoBack()) {
                cusWebView!!.goBack()
                //                int size = mStrings.size();
                //                KLog.d("size:" + size + ",mHistory:" + mHistory);
                //                if (size > 1) {
                //                    mStrings.remove(size - 1);
                //                    KLog.d("size:" + mStrings.size());
                //                    size = mStrings.size();
                //                    String url;
                //                    if (size == 1) {
                //                        url = mUrl;
                //                    } else {
                //                        url = mStrings.get(size - 1);
                //                    }
                //                    mWebView.loadUrl(url);
                return true
                //                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun collectUrl() {
        val urlCollect = UrlCollect(null, url, title, Date(), type, author)
        //        mUrlCollectDao.insert(urlCollect);
    }

    private fun cancelCollect() {
        //        mUrlCollectDao.deleteByKey(mUrlCollect.getId());
    }


    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (!TextUtils.isEmpty(url)) {
                cusWebView!!.loadUrl(url)
            }
            return true
        }

        override fun shouldInterceptRequest(view: WebView,
                                            request: com.tencent.smtt.export.external.interfaces.WebResourceRequest): WebResourceResponse {
            Log.e("should", "request.getUrl().toString() is " + request.url.toString())
            return super.shouldInterceptRequest(view, request)
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            history = url
            if (!TextUtils.isEmpty(url)) {
                if (!strings.contains(url)) {
                    strings.add(url!!)
                }
            }
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {
        private var myVideoView: View? = null
        private var myNormalView: View? = null
        internal var callback: IX5WebChromeClient.CustomViewCallback? = null

        override fun onJsConfirm(arg0: WebView?, arg1: String?, arg2: String?, arg3: com.tencent.smtt.export.external.interfaces.JsResult?): Boolean {
            return super.onJsConfirm(arg0, arg1, arg2, arg3)
        }

        override fun onProgressChanged(webView: WebView?, newProgress: Int) {
            if (web_progress_bar == null) {
                return
            }
            web_progress_bar!!.progress = newProgress

            if (newProgress == 100) {
                web_progress_bar!!.visibility = View.GONE
            } else {
                web_progress_bar!!.visibility = View.VISIBLE
            }
            super.onProgressChanged(webView, newProgress)
        }

        /**
         * 全屏播放配置
         */
        override fun onShowCustomView(view: View?, customViewCallback: IX5WebChromeClient.CustomViewCallback?) {
            val normalView = findViewById<View>(R.id.web_filechooser) as FrameLayout
            val viewGroup = normalView.parent as ViewGroup
            viewGroup.removeView(normalView)
            viewGroup.addView(view)
            myVideoView = view
            myNormalView = normalView
            callback = customViewCallback
        }

        override fun onHideCustomView() {
            if (callback != null) {
                callback!!.onCustomViewHidden()
                callback = null
            }
            if (myVideoView != null) {
                val viewGroup = myVideoView!!.parent as ViewGroup
                viewGroup.removeView(myVideoView)
                viewGroup.addView(myNormalView)
            }
        }

        override fun onShowFileChooser(arg0: WebView?,
                                       arg1: ValueCallback<Array<Uri>>?, arg2: WebChromeClient.FileChooserParams?): Boolean {
            // TODO Auto-generated method stub
            Log.e("app", "onShowFileChooser")
            return super.onShowFileChooser(arg0, arg1, arg2)
        }

        override fun openFileChooser(uploadFile: ValueCallback<Uri>, acceptType: String?, captureType: String?) {
            //            JiandanWebActivity.this.uploadFile = uploadFile;
            //            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            //            i.addCategory(Intent.CATEGORY_OPENABLE);
            //            i.setType("*/*");
            //            startActivityForResult(Intent.createChooser(i, "test"), 0);
        }


        override fun onJsAlert(arg0: WebView?, arg1: String?, arg2: String?, arg3: com.tencent.smtt.export.external.interfaces.JsResult?): Boolean {
            /**
             * 这里写入你自定义的window alert
             */
            // AlertDialog.DownloadParams builder = new DownloadParams(getContext());
            // builder.setTitle("X5内核");
            // builder.setPositiveButton("确定", new
            // DialogInterface.OnClickListener() {
            //
            // @Override
            // public void onClick(DialogInterface dialog, int which) {
            // // TODO Auto-generated method stub
            // dialog.dismiss();
            // }
            // });
            // builder.show();
            // arg3.confirm();
            // return true;
            Log.i("yuanhaizhou", "setX5webview = null")
            return super.onJsAlert(null, "www.baidu.com", "aa", arg3)
        }

        /**
         * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
         */


        override fun onReceivedTitle(arg0: WebView?, arg1: String?) {
            super.onReceivedTitle(arg0, arg1)
            Log.i("yuanhaizhou", "webpage title is " + arg1!!)

        }
    }

    override fun onStop() {
        super.onStop()
        if (states == CollectStates.COLLECT) {
            collectUrl()
        } else if (states == CollectStates.UN_COLLECT) {
            cancelCollect()
        }
    }

    override fun onDestroy() {
        if (cusWebView != null) {
            cusWebView!!.destroy()
            cusWebView = null
        }
        super.onDestroy()
    }

    companion object {
        const val FROM_COLLECT = 1
        const val FROM_JIANDAN = 2

        private val timeout = 50 * 1000
        private val USERAGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"

        const val TITLE = "title"
        const val URL = "url"
        const val TYPE = "type"
        const val AUTHOR = "author"
        const val FROM_WAY = "from_type"

        fun startWebActivity(activity: Activity, bundle: Bundle?) {
            val intent = Intent(activity, JiandanWebActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}
