package com.left.gank.ui.daily

import android.business.domain.Girl
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.utils.ListUtils
import kotlinx.android.synthetic.main.fragment_refresh.*

class DailyGirlFragmentKt : LazyFragment(), DailyGirlContract.View {
    private lateinit var dailyGirlAdapter: DailyGirlAdapter
    private lateinit var presenter: DailyGirlPresenter

    override fun fragmentLayoutId(): Int = R.layout.fragment_refresh

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyGirlAdapter = DailyGirlAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dailyGirlAdapter
        }
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    override fun onLazyActivityCreate() {
        presenter = DailyGirlPresenter(context!!, this)
        loadData()
    }

    private fun loadData() {
        presenter.loadGirls()
    }

    override fun loadDailyGirlSuccess(list: List<Girl>) {
        if (ListUtils.isEmpty(list)) {
            showContent()
            return
        }
        showContent()
        dailyGirlAdapter.apply {
            dailyGirlAdapter.refillItem(list)
        }
    }

    override fun loadDailyGirlFailure(msg: String) {
        showEmpty()
    }

    override fun showContent() {
        super.showContent()
        multiple_status_view?.showContent()
    }

    override fun showEmpty() {
        super.showEmpty()
        multiple_status_view?.showEmpty()
    }

    override fun showProgress() {
        super.showProgress()
        swipe_refresh?.isRefreshing = true
    }

    override fun hideProgress() {
        super.hideProgress()
        swipe_refresh?.isRefreshing = false
    }
}