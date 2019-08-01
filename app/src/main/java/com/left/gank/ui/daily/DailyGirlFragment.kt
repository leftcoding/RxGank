package com.left.gank.ui.daily

import android.app.ProgressDialog
import android.business.domain.Gift
import android.business.domain.Girl
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.ui.gallery.GalleryActivity
import com.left.gank.utils.ListUtils
import kotlinx.android.synthetic.main.fragment_refresh.*
import java.util.*

class DailyGirlFragment : LazyFragment(), DailyGirlContract.View {
    private lateinit var dailyGirlAdapter: DailyGirlAdapter
    private lateinit var presenter: DailyGirlContract.Presenter
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
            layoutManager = LinearLayoutManager(context)
            adapter = dailyGirlAdapter
        }
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    override fun onLazyActivityCreate() {
        presenter = DailyGirlPresenter(context!!, this)
        presenter.setLifeCycleOwner(this)
        loadData()
    }

    private val listener = object : DailyGirlAdapter.ItemCallback {
        override fun onItemClick(girl: Girl) {
            presenter.getImages(girl.url)
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
            refillItem(list)
            notifyDataSetChanged()
        }
    }

    override fun loadDailyGirlFailure(msg: String) {
        showEmpty()
    }

    override fun loadImagesFailure(msg: String) {
        shortToast(msg)
    }

    override fun loadImagesSuccess(list: List<Gift>) {
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