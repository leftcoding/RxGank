package com.left.gank.ui.discovered.jiandan

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.domain.JianDanBean
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.web.JiandanWebActivity
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_refresh.*

/**
 * 新鲜事
 * Create by LingYan on 2016-11-18
 */

class JiandanFragment : LazyFragment(), JiandanContract.View {
    private lateinit var jianDanAdapter: JiandanAdapter
    private lateinit var presenter: JiandanContract.Presenter
    private var curPage = 1

    override fun fragmentLayoutId(): Int = R.layout.fragment_refresh

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            curPage = 1
            presenter.loadJianDan(curPage)
        }

        override fun onLoadMore() {
            presenter.loadJianDan(curPage)
        }
    }

    private val callback = object : JiandanAdapter.Callback {
        override fun onClick(result: JianDanBean) {
            Bundle().apply {
                putString(JiandanWebActivity.TITLE, result.title)
                putString(JiandanWebActivity.URL, result.url)
                putString(JiandanWebActivity.TYPE, Constants.JIANDAN)
                putString(JiandanWebActivity.AUTHOR, result.type)
                JiandanWebActivity.startWebActivity(activity, this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jianDanAdapter = JiandanAdapter()
        jianDanAdapter.setListener(callback)

        recycler_view.apply {
            adapter = jianDanAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(MyDecoration(context, LinearLayoutManager.HORIZONTAL))
            setHasFixedSize(true)
            val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh)
            onFlexibleScrollListener.setOnScrollListener(scrollListener)
            addOnScrollListener(onFlexibleScrollListener)
        }
    }

    override fun onLazyActivityCreate() {
        presenter = JiandanPresenter(context!!, this)
        presenter.loadJianDan(curPage)
    }

    override fun loadJiandanSuccess(page: Int, list: List<JianDanBean>?) {
        list?.let {
            if (page == 1) {
                jianDanAdapter.updateItem(list)
            } else {
                jianDanAdapter.appendItem(list)
            }
            curPage = page + 1
        }
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
}
