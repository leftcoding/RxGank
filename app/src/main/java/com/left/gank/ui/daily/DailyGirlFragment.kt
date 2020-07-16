package com.left.gank.ui.daily

import android.app.ProgressDialog
import android.business.domain.Gift
import android.business.domain.Girl
import android.business.domain.PageConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.ui.gallery.GalleryActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener
import kotlinx.android.synthetic.main.fragment_refresh.*
import java.util.*

class DailyGirlFragment : LazyFragment(), DailyGirlContract.View {
    private lateinit var dailyGirlAdapter: DailyGirlAdapter
    private lateinit var presenter: DailyGirlContract.Presenter
    private val pageConfig = PageConfig()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DailyViewModel::class.java)
    }
    private val progressDialog by lazy {
        ProgressDialog(context).apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(context.getString(R.string.loading_meizi_images))
            isIndeterminate = true
            setCanceledOnTouchOutside(false)
        }
    }

    override fun fragmentLayoutId(): Int = R.layout.fragment_refresh

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyGirlAdapter = DailyGirlAdapter()
        dailyGirlAdapter.setOnItemClickListener(listener)

        recycler_view.apply {
            adapter = dailyGirlAdapter
            layoutManager = LinearLayoutManager(context)
            val onFlexibleScrollListener = OnFlexibleScrollListener(swipe_refresh)
            onFlexibleScrollListener.setOnScrollListener(scrollListener)
            addOnScrollListener(onFlexibleScrollListener)
        }
    }

    override fun onLazyActivityCreate() {
        presenter = DailyGirlPresenter(context!!, this)
        presenter.setLifeCycleOwner(this)
        loadData(PageConfig.starPage())
    }

    private val scrollListener = object : OnFlexibleScrollListener.ScrollListener {
        override fun onRefresh() {
            loadData(PageConfig.starPage())
        }

        override fun onLoadMore() {
            loadData(pageConfig.nextPage)
        }
    }

    private val listener = object : DailyGirlAdapter.ItemClickListener {
        override fun onItemClick(girl: Girl, view: View) {
            val list = arrayListOf<Gift>(Gift(girl.url))
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putParcelableArrayListExtra(GalleryActivity.EXTRA_LIST, list)
            val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, getString(R.string.transition_welfare_image))
            startActivity(intent, activityOptionsCompat.toBundle())
        }
    }

    override fun hideLoadingDialog() {
        if (progressDialog.isShowing) {
            progressDialog.hide()
        }
    }

    override fun showLoadingDialog() {
        progressDialog.show()
    }

    private fun loadData(page: Int) {
        presenter.loadGirls(page)
    }

    override fun loadDailyGirlSuccess(list: List<Girl>, page: Int) {
        if (ListUtils.isEmpty(list)) {
            showContent()
            return
        }
        showContent()
        pageConfig.curPage = page
        dailyGirlAdapter.apply {
            when (page) {
                1 -> {
                    refillItem(list)
                }
                else -> {
                    appendItem(list)
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun loadDailyGirlFailure(msg: String) {
        showEmpty()
    }

    override fun loadImagesFailure(msg: String) {
        shortToast(msg)
    }

    override fun loadImagesSuccess(list: List<Gift>, url: String) {
        if (list.isNotEmpty()) {
            Intent(context, GalleryActivity::class.java).apply {
                putExtra(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY)
                putExtra(GalleryActivity.EXTRA_LIST, list as ArrayList<*>)
                startActivity(this)
            }
        }
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