package com.left.gank.ui.android

import android.business.domain.Gank
import android.business.domain.PageConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.web.normal.WebActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_android.*

/**
 * Create by LingYan on 2016-4-26
 */
class AndroidFragment : LazyFragment(), AndroidContract.View {
    private lateinit var androidAdapter: AndroidAdapter
    private lateinit var androidPresenter: AndroidContract.Presenter

    private val pageConfig = PageConfig()

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_android
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        androidAdapter = AndroidAdapter(context!!).apply {
            setErrorListener(errorListener)
            setCallback(callback)
        }

        recycler_view.apply {
            adapter = androidAdapter
            layoutManager = LinearLayoutManager(context)
            val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh)
            onFlexibleScrollListener.setOnScrollListener(scrollListener)
            addOnScrollListener(onFlexibleScrollListener)
        }

        multiple_status_view.setListener {
            showLoading()
            loadAndroid(true, PageConfig.starPage())
        }
    }

    override fun onLazyActivityCreate() {
        androidPresenter = AndroidPresenter(context!!, this).apply {
            setLifeCycleOwner(this@AndroidFragment)
        }
        loadAndroid(true, PageConfig.starPage())
    }

    private val errorListener = {
        loadAndroid(false, pageConfig.nextPage)
    }

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadAndroid(false, PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadAndroid(false, pageConfig.nextPage)
        }
    }

    private val callback = object : AndroidAdapter.Callback {
        override fun onItemClick(view: View, gank: Gank) {
            val intent = Intent(context, WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString(WebActivity.TITLE, gank.desc)
            bundle.putString(WebActivity.URL, gank.url)
            bundle.putString(WebActivity.TYPE, Constants.ANDROID)
            bundle.putString(WebActivity.AUTHOR, gank.who)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun loadAndroid(useProgress: Boolean, page: Int) {
        androidPresenter.loadAndroid(true, useProgress, page)
    }

    override fun showProgress() {
        if (!swipe_refresh.isRefreshing) {
            showLoading()
        }
    }

    override fun hideProgress() {
        swipe_refresh?.isRefreshing = false
    }

    override fun showContent() {
        multiple_status_view?.showContent()
    }

    override fun showEmpty() {
        multiple_status_view?.showEmpty()
    }

    private fun showLoading() {
        multiple_status_view?.showLoading()
    }

    override fun loadAndroidSuccess(page: Int, list: List<Gank>?) {
        showContent()
        val isFirst = PageConfig.isFirstPage(page)
        if (ListUtils.isEmpty(list) && isFirst) {
            showEmpty()
            return
        }
        pageConfig.curPage = page
        androidAdapter.apply {
            if (isFirst) fillItems(list) else appendItems(list)
            notifyDataSetChanged()
        }
    }

    override fun loadAndroidFailure(page: Int, msg: String) {
        shortToast(msg)
        val isFirst = PageConfig.isFirstPage(page)
        if (isFirst) {
            showEmpty()
            return
        }
        androidAdapter.apply {
            showError()
            notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        if (::androidAdapter.isInitialized) {
            androidAdapter.destroy()
        }
        if (::androidPresenter.isInitialized) {
            androidPresenter.destroy()
        }
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): AndroidFragment = AndroidFragment()
    }
}
