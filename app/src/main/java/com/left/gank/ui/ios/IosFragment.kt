package com.left.gank.ui.ios

import android.business.domain.Gank
import android.business.domain.PageConfig
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.config.Constants
import com.left.gank.ui.ios.text.ItemCallback
import com.left.gank.ui.web.normal.WebActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.MultipleStatusView
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_android.*

/**
 * ios
 * Create by LingYan on 2016-4-26
 */
class IosFragment : LazyFragment(), IosContract.View {
    private lateinit var iosAdapter: IosAdapter
    private lateinit var iosPresenter: IosContract.Presenter
    private val pageConfig = PageConfig()

    private val multipleClick = MultipleStatusView.OnMultipleClick { loadIos(false, PageConfig.starPage()) }

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadIos(true, PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadIos(false, pageConfig.nextPage)
        }
    }

    private val onItemClickListener = object : ItemCallback {
        override fun onItemClick(view: View, gank: Gank) {
            Bundle().apply {
                putString(WebActivity.TITLE, gank.desc)
                putString(WebActivity.URL, gank.url)
                putString(WebActivity.AUTHOR, gank.who)
                putString(WebActivity.TYPE, Constants.IOS)
                WebActivity.startWebActivity(context!!, this)
            }
        }
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_welfare
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iosAdapter = IosAdapter(context!!)
        iosAdapter.setOnItemClickListener(onItemClickListener)

        val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh!!)
        onFlexibleScrollListener.setOnScrollListener(scrollListener)
        recycler_view!!.addOnScrollListener(onFlexibleScrollListener)
        recycler_view!!.layoutManager = LinearLayoutManager(context)
        recycler_view!!.adapter = iosAdapter
        multiple_status_view!!.setListener(multipleClick)
    }

    override fun onLazyActivityCreate() {
        iosPresenter = IosPresenter(context!!, this).apply {
            setLifeCycleOwner(this@IosFragment)
        }
        loadIos(true, PageConfig.starPage())
    }

    override fun showEmpty() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showEmpty()
        }
    }

    override fun showProgress() {
        if (swipe_refresh != null && !swipe_refresh!!.isRefreshing) {
            if (multiple_status_view != null) {
                multiple_status_view!!.showLoading()
            }
            return
        }

        if (swipe_refresh != null) {
            swipe_refresh!!.isRefreshing = true
        }
    }

    override fun hideProgress() {
        if (swipe_refresh != null) {
            swipe_refresh!!.isRefreshing = false
        }
    }

    override fun showContent() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showContent()
        }
    }

    private fun loadIos(useProgress: Boolean, page: Int) {
        iosPresenter.loadIos(true, useProgress, page)
    }

    override fun loadIosSuccess(page: Int, list: List<Gank>?) {
        showContent()

        if (ListUtils.isEmpty(list)) {
            showEmpty()
            return
        }
        pageConfig.curPage = page
        list?.let {
            if (PageConfig.isFirstPage(page)) {
                iosAdapter.fillItems(it)
            } else {
                iosAdapter.appendItems(it)
            }
            iosAdapter.notifyDataSetChanged()
        }
    }

    override fun loadIosFailure(page: Int, msg: String) {
        if (!PageConfig.isFirstPage(page)) {
            iosAdapter.showError()
            iosAdapter.notifyDataSetChanged()
        } else {
            if (multiple_status_view != null) {
                multiple_status_view!!.showError()
            }
        }
    }

    override fun onDestroyView() {
        if (::iosPresenter.isInitialized) {
            iosPresenter.destroy()
        }
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): IosFragment = IosFragment()
    }
}
