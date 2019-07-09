package com.left.gank.ui.discovered.jiandan

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.domain.JianDanBean
import com.left.gank.listener.ItemClick
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.web.JiandanWebActivity
import com.left.gank.widget.MyDecoration
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_refresh.*

/**
 * 新鲜事
 * Create by LingYan on 2016-11-18
 */

class JiandanFragment : LazyFragment(), JiandanContract.View, ItemClick {
    private lateinit var jianDanAdapter: JiandanAdapter
    private lateinit var presenter: JiandanContract.Presenter
    private var page = 1

    override fun fragmentLayoutId(): Int = R.layout.fragment_refresh

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            presenter.loadJianDan(page)
        }

        override fun onLoadMore() {
            presenter.loadJianDan(page)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jianDanAdapter = JiandanAdapter()
        jianDanAdapter.setListener(this)

        recycler_view.apply {
            adapter = jianDanAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MyDecoration(context, LinearLayoutManager.HORIZONTAL))
            setHasFixedSize(true)
            val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh)
            onFlexibleScrollListener.setOnScrollListener(scrollListener)
            addOnScrollListener(onFlexibleScrollListener)
        }
    }

    override fun onLazyActivityCreate() {
        presenter = JianDanPresenter(context!!, this)
        presenter.loadJianDan(page)
    }

    override fun loadJiandanSuccess(page: Int, list: List<JianDanBean>?) {
        jianDanAdapter.updateItem(list)
    }

    override fun loadJiandanFailure(page: Int, msg: String) {
        shortToast(msg)
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

    override fun onClick(position: Int, `object`: Any) {
        val bean = `object` as JianDanBean
        val bundle = Bundle()
        bundle.putString(JiandanWebActivity.TITLE, bean.title)
        bundle.putString(JiandanWebActivity.URL, bean.url)
        bundle.putString(JiandanWebActivity.TYPE, Constants.JIANDAN)
        bundle.putString(JiandanWebActivity.AUTHOR, bean.type)
        JiandanWebActivity.startWebActivity(activity, bundle)
    }
}
