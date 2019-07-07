package com.left.gank.ui.welfare

import android.business.domain.Gank
import android.business.domain.Gift
import android.business.domain.PageConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.left.gank.R
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.gallery.GalleryActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.MultipleStatusView
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_android.*

/**
 * 干货 - 妹子
 * Create by LingYan on 2016-5-12
 */
class WelfareFragment : LazyFragment(), WelfareContract.View {
    private lateinit var welfareAdapter: WelfareAdapter
    private lateinit var presenter: WelfareContract.Presenter
    private val pageConfig = PageConfig()

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadWelfare(true, PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadWelfare(false, pageConfig.nextPage)
        }
    }

    private val onMultipleClick = MultipleStatusView.OnMultipleClick { multiple_status_view!!.showLoading() }

    private val itemClickListener = object : WelfareAdapter.ItemClickListener {
        override fun onItem(view: View, url: String) {
            val list = listOf(Gift(url))
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putParcelableArrayListExtra(GalleryActivity.EXTRA_LIST, ArrayList(list))
            startActivity(intent)
        }
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_welfare
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welfareAdapter = WelfareAdapter(context!!)
        welfareAdapter.setListener(itemClickListener)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
        recycler_view!!.layoutManager = staggeredGridLayoutManager
        val scrollListener = OnFlexibleScrollListener(swipe_refresh!!)
        scrollListener.setOnScrollListener(this.scrollListener)
        recycler_view!!.addOnScrollListener(scrollListener)
        recycler_view!!.adapter = welfareAdapter

        multiple_status_view!!.setListener(onMultipleClick)
    }

    override fun onLazyActivityCreate() {
        presenter = WelfarePresenter(context!!, this)
        loadWelfare(true, PageConfig.starPage())
    }

    private fun loadWelfare(useProgress: Boolean, page: Int) {
        presenter.loadWelfare(true, useProgress, page)
    }

    override fun hideProgress() {
        if (swipe_refresh != null) {
            swipe_refresh!!.isRefreshing = false
        }
    }

    override fun showProgress() {
        if (swipe_refresh != null && !swipe_refresh!!.isRefreshing) {
            showLoading()
        }
    }

    private fun showLoading() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showLoading()
        }
    }

    override fun showContent() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showContent()
        }
    }

    override fun showEmpty() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showEmpty()
        }
    }

    override fun loadWelfareSuccess(page: Int, list: List<Gank>?) {
        val isFirst = PageConfig.isFirstPage(page)
        if (isFirst && ListUtils.isEmpty(list)) {
            showEmpty()
            return
        }

        showContent()
        pageConfig.curPage = page
        list?.let {
            if (isFirst) {
                welfareAdapter.fillItems(it)
            } else {
                welfareAdapter.appendItems(it)
            }
            welfareAdapter.notifyDataSetChanged()
        }
    }

    override fun loadWelfareFailure(page: Int, msg: String) {
        shortToast(msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    companion object {

        fun newInstance(): WelfareFragment {
            val fragment = WelfareFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}