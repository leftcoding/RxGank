package com.left.gank.ui.discovered.video

import android.business.domain.Gank
import android.business.domain.PageConfig
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.listener.MeiziOnClick
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.web.WebVideoViewActivity
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_refresh.*

/**
 * 休息视频
 * Create by LingYan on 2016-04-25
 */
class VideoFragment : LazyFragment(), VideoContract.View {
    private lateinit var presenter: VideoContract.Presenter
    private lateinit var videoAdapter: VideoAdapter
    private val pageConfig = PageConfig()

    override fun fragmentLayoutId(): Int = R.layout.fragment_refresh

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadVideo(PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadVideo(pageConfig.nextPage)
        }
    }

    private val itemClickListener = MeiziOnClick { _, position ->
        Bundle().apply {
            val list = videoAdapter.results
            putString(WebVideoViewActivity.TITLE, list[position].desc)
            putString(WebVideoViewActivity.URL, list[position].url)
            WebVideoViewActivity.startWebActivity(activity, this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        multiple_status_view.setListener { loadVideo(pageConfig.curPage) }

        videoAdapter = VideoAdapter(activity).apply {
            setOnItemClickListener(itemClickListener)
        }

        recycler_view.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
            val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh)
            onFlexibleScrollListener.setOnScrollListener(scrollListener)
            addOnScrollListener(onFlexibleScrollListener)
        }
    }

    override fun onLazyActivityCreate() {
        presenter = VideoPresenter(context!!, this)
        presenter.setLifeCycleOwner(this)
        loadVideo(PageConfig.starPage())
    }

    override fun loadVideoSuccess(page: Int, list: List<Gank>) {
        list.let {
            if (PageConfig.isFirstPage(page)) {
                videoAdapter.refillItems(it)
            } else {
                videoAdapter.appendItems(it)
            }
        }
    }

    override fun loadVideoFailure(page: Int, msg: String) {
        shortToast(msg)
    }

    private fun loadVideo(page: Int) {
        presenter.loadVideo(true, true, page)
    }

    override fun showProgress() {
        swipe_refresh?.isRefreshing = true
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
}
