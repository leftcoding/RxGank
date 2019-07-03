package com.left.gank.ui.ios

import android.business.domain.Gank
import android.business.domain.PageConfig
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.web.normal.WebActivity
import com.left.gank.widget.MultipleStatusView
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener

/**
 *
 * Create by LingYan on 2019-06-27
 */
class IosFragmentKt : LazyFragment(), IosContract.View {
    @BindView(R.id.multiple_status_view)
    internal var multipleStatusView: MultipleStatusView? = null

    @BindView(R.id.swipe_refresh)
    internal var swipeRefreshLayout: SwipeRefreshLayout? = null

    @BindView(R.id.recycler_view)
    internal var recyclerView: RecyclerView? = null

    private lateinit var iosAdapter: IosAdapter
    private lateinit var iosPresenter: IosContract.Presenter
    private val pageConfig = PageConfig()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iosAdapter = IosAdapter(context).apply {
            setOnItemClickListener { _, gank ->
                startWebActivity(gank)
            }
        }

        val onFlexibleScrollListener = OnFlexibleScrollListener(swipeRefreshLayout)
        onFlexibleScrollListener.setOnScrollListener(scrollListener)
        recyclerView!!.apply {
            addOnScrollListener(onFlexibleScrollListener)
            layoutManager = LinearLayoutManager(context)
            adapter = this@IosFragmentKt.iosAdapter
        }

        multipleStatusView!!.setListener(multipleClick)
    }

    override fun onLazyActivityCreate() {
        iosPresenter = IosPresenter(context, this)
        loadIos(true, PageConfig.starPage())
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_welfare
    }

    private val multipleClick = MultipleStatusView.OnMultipleClick { loadIos(false, PageConfig.starPage()) }

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadIos(true, PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadIos(false, pageConfig.nextPage)
        }
    }

    private fun loadIos(useProgress: Boolean, page: Int) {
        iosPresenter.loadIos(true, useProgress, page)
    }

    override fun loadIosSuccess(page: Int, list: MutableList<Gank>?) {
        showContent()

        if (list == null || list.isEmpty()) {
            showEmpty()
            return
        }
        pageConfig.curPage = page
        if (PageConfig.isFirstPage(page)) {
            iosAdapter.fillItems(list)
        } else {
            iosAdapter.appendItems(list)
        }
        iosAdapter.notifyDataSetChanged()
    }

    override fun loadIosFailure(page: Int, msg: String?) {
        context?.let {
            if (!PageConfig.isFirstPage(page)) {
                iosAdapter.apply {
                    showError()
                    notifyDataSetChanged()
                }
            } else {
                multipleStatusView?.showError()
            }
        }
    }

    private fun startWebActivity(gank: Gank?) {
        val bundle = Bundle().apply {
            putString(WebActivity.TITLE, gank?.desc)
            putString(WebActivity.URL, gank?.url)
            putString(WebActivity.AUTHOR, gank?.who)
            putString(WebActivity.TYPE, Constants.IOS)
        }
        WebActivity.startWebActivity(context, bundle)
    }

    override fun showContent() {
        super.showContent()
        multipleStatusView?.showContent()
    }

    override fun showEmpty() {
        super.showEmpty()
        multipleStatusView?.showEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        iosPresenter.destroy()
        iosAdapter.destroy()
    }

    companion object {
        fun newInstance() = IosFragment()
    }
}