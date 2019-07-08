package com.left.gank.ui.web.normal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.*
import android.widget.FrameLayout
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.data.entity.ReadHistory
import com.left.gank.data.entity.UrlCollect
import com.left.gank.mvp.source.LocalDataSource
import com.left.gank.ui.base.activity.BaseActivity
import com.left.gank.utils.AppUtils
import com.left.gank.utils.ShareUtils
import com.left.gank.utils.ToastUtils
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.sdk.*
import kotlinx.android.synthetic.main.activity_web.*
import java.util.*

/**
 * 普通webView
 * Create by LingYan on 2016-5-10
 */
class WebActivity : BaseActivity(), WebContract.View {
    private var webView: WebView? = null
    private var url: String? = null
    private var title: String? = null
    private var collectType: String? = null
    private var author: String? = null
    private var isCollect: Boolean = false
    private var isInitCollect: Boolean = false
    private var mStates = CollectStates.NORMAL
    private var mFromType: Int = 0
    private var mPresenter: WebContract.Presenter? = null
    private var mMenuItem: MenuItem? = null

    private var uploadFile: ValueCallback<Uri>? = null

    override val collect: UrlCollect
        get() = UrlCollect(null, url, title, Date(), collectType, author)

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun showEmpty() {

    }

    override fun showContent() {

    }

    internal enum class CollectStates {
        NORMAL, COLLECT, UN_COLLECT
    }

    override fun getContentId(): Int = R.layout.activity_web

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseBundle()
        mPresenter = WebPresenter(LocalDataSource.getInstance(), this)

        webView = WebView(applicationContext, null)

        web_view!!.addView(webView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT))

        val settings = webView!!.settings
        webView!!.requestFocusFromTouch() //支持获取手势焦点，输入用户名、密码或其他
        settings.javaScriptEnabled = true  //支持js
        settings.domStorageEnabled = true //
        settings.setSupportZoom(true) //设置支持缩放
        settings.builtInZoomControls = true //
        settings.displayZoomControls = false//是否显示缩放控件
        settings.useWideViewPort = true  //将图片调整到适合webview的大小
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //支持内容重新布局
        settings.allowFileAccess = true  //设置可以访问文件
        settings.setNeedInitialFocus(true) //当webview调用requestFocus时为webview设置节点
        settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        settings.loadsImagesAutomatically = true  //支持自动加载图片
        settings.defaultTextEncodingName = "utf-8"//设置编码格式

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        settings.setSupportMultipleWindows(false)
        settings.setAppCacheEnabled(true)
        settings.setGeolocationEnabled(true)
        settings.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        settings.setAppCachePath(this.getDir("appcache", 0).path)
        settings.databasePath = this.getDir("databases", 0).path
        settings.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .path)
        settings.pluginState = WebSettings.PluginState.ON_DEMAND

        webView!!.setWebViewClient(MyWebViewClient())
        webView!!.setWebChromeClient(MyWebChromeClient())
        webView!!.loadUrl(url)

        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().sync()

        setSupportActionBar(toolbar)
        setTitle(title)
        toolbar!!.setNavigationOnClickListener { finish() }

        isInitCollect = true
        if (!TextUtils.isEmpty(url)) {
            if (mPresenter != null) {
                mPresenter!!.findCollectUrl(url!!)
                mPresenter!!.insetHistoryUrl(ReadHistory(null, url, title, Date(), collectType))
            }
        }
    }

    private fun parseBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            url = bundle.getString(URL)
            title = bundle.getString(TITLE)
            collectType = bundle.getString(TYPE, Constants.ALL)
            author = bundle.getString(AUTHOR)
            mFromType = bundle.getInt(FROM_TYPE)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mMenuItem = menu.findItem(R.id.welfare_collect)
        switchCollectIcon(isCollect)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.welfare_collect -> {
                mStates = CollectStates.NORMAL
                isCollect = !isCollect
                mPresenter!!.collectAction(isCollect)
                switchCollectIcon(isCollect)
                return true
            }
            R.id.welfare_share -> {
                ShareUtils.getInstance().shareText(this, webView!!.title, webView!!.url)
                return true
            }
            R.id.welfare_copy_url -> {
                AppUtils.copyText(this, webView!!.url)
                ToastUtils.showToast(baseContext, R.string.tip_copy_success)
                return true
            }
            R.id.welfare_refresh -> {
                webView!!.reload()
                return true
            }
            R.id.welfare_browser -> {
                openBrowser(webView!!.url)
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchCollectIcon(isCollect: Boolean) {
        if (isCollect) {
            mMenuItem!!.setIcon(R.drawable.navigation_collect_prs)
        } else {
            mMenuItem!!.setIcon(R.drawable.navigation_collect_nor)
        }
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        if (intent.resolveActivity(this@WebActivity.packageManager) != null) {
            this@WebActivity.startActivity(intent)
        } else {
            ToastUtils.showToast(baseContext, R.string.web_open_failed)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView!!.canGoBack()) {
                webView!!.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // 这些视频需要hack CSS才能达到全屏播放的效果
            url?.apply {
                when {
                    url.contains("www.vmovier.com") -> injectCSS("vmovier.css")
                    url.contains("video.weibo.com") -> injectCSS("weibo.css")
                    url.contains("m.miaopai.com") -> injectCSS("miaopai.css")
                }
            }
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {

        private var myVideoView: View? = null
        private var myNormalView: View? = null
        internal var callback: IX5WebChromeClient.CustomViewCallback? = null

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

        override fun openFileChooser(uploadFile: ValueCallback<Uri>, acceptType: String?, captureType: String?) {
            this@WebActivity.uploadFile = uploadFile
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "*/*"
            startActivityForResult(Intent.createChooser(i, "test"), 0)
        }


        override fun onJsAlert(arg0: WebView?, arg1: String?, arg2: String?, arg3: com.tencent.smtt.export.external.interfaces.JsResult?): Boolean {
            return super.onJsAlert(null, "www.baidu.com", "aa", arg3)
        }

        /**
         * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
         */
        override fun onReceivedTitle(arg0: WebView?, arg1: String?) {
            super.onReceivedTitle(arg0, arg1)
        }
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private fun injectCSS(filename: String) {
        try {
            val inputStream = this.assets.open(filename)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
            webView!!.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        if (webView != null) {
            val viewParent = webView!!.parent
            if (viewParent != null) {
                (viewParent as ViewGroup).removeView(webView)
            }
            webView!!.stopLoading()
            webView!!.settings.javaScriptEnabled = false
            webView!!.clearView()
            webView!!.clearFormData()
            webView!!.clearHistory()
            webView!!.removeAllViews()
            webView!!.destroy()
            webView = null
        }
        super.onDestroy() // All you have to do is destroy() the WebView before Activity finishes
    }

    override fun onCollect() {
        switchCollectIcon(true)
    }

    override fun onCancelCollect() {
        switchCollectIcon(false)
    }

    override fun setCollectIcon(isCollect: Boolean) {
        invalidateOptionsMenu()//更新Menu
        this.isCollect = isCollect
    }

    companion object {
        const val FROM_MAIN = 0
        const val FROM_COLLECT = 1
        const val TITLE = "title"
        const val URL = "url"
        const val TYPE = "type"
        const val AUTHOR = "author"
        const val FROM_TYPE = "from_type"

        fun startWebActivity(context: Context, bundle: Bundle?) {
            val intent = Intent(context, WebActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}
