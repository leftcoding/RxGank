package com.left.gank.ui.cure

import android.app.ProgressDialog
import android.business.domain.Gift
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.ui.gallery.GalleryActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.LySwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_gift.*
import java.util.*

/**
 * 妹子 - 清纯
 * Create by LingYan on 2016-07-01
 */
class CureFragment : LazyFragment(), CureContract.View {
    private lateinit var cureAdapter: CureAdapter
    private lateinit var curePresenter: CureContract.Presenter
    private lateinit var progressDialog: ProgressDialog
    private var page = 1

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_gift
    }

    private val cureCallback = object : CureAdapter.Callback {
        override fun onClick(url: String) {
            !TextUtils.isEmpty(url).apply {
                curePresenter.loadImages(url)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cureAdapter = CureAdapter().apply {
            setOnItemClickListener(cureCallback)
        }

        progressDialog = ProgressDialog(context).apply {
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage(context.getString(R.string.loading_meizi_images))
            isIndeterminate = true
            setCanceledOnTouchOutside(true)
            setOnCancelListener { curePresenter.destroy() }
        }

        swipe_refresh!!.apply {
            setLayoutManager(LinearLayoutManager(context))
            setAdapter(cureAdapter)
            setOnScrollListener(object : LySwipeRefreshLayout.OnListener {
                override fun onRefresh() {
                    page = 1
                    loadData()
                }

                override fun onLoadMore() {
                    loadData()
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        curePresenter = CurePresenter(context!!, this).apply {
            setLifeCycleOwner(this@CureFragment)
        }
    }

    private fun loadData() {
        curePresenter.loadData(page)
    }

    override fun showLoadingDialog() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    override fun hideLoadingDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun loadImagesSuccess(list: List<Gift>) {
        Intent(context, GalleryActivity::class.java).apply {
            putExtra(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY)
            putExtra(GalleryActivity.EXTRA_LIST, list as ArrayList<*>)
            startActivity(this)
        }
    }

    override fun loadImagesFailure(msg: String) {
        shortToast(msg)
    }

    override fun hideProgress() {
        if (swipe_refresh != null && swipe_refresh!!.isRefreshing) {
            swipe_refresh!!.isRefreshing = false
        }
    }

    override fun showProgress() {
        if (swipe_refresh != null && !swipe_refresh!!.isRefreshing) {
            swipe_refresh!!.isRefreshing = true
        }
    }

    override fun showContent() {
        if (multiple_status_view != null) {
            multiple_status_view!!.showContent()
        }
    }

    override fun loadDataSuccess(page: Int, maxPage: Int, list: List<Gift>) {
        if (ListUtils.isNotEmpty(list)) {
            this.page = page + 1
            if (page == 1) {
                cureAdapter.refillItem(list)
            } else {
                cureAdapter.appendItem(list)
            }
            cureAdapter.notifyDataSetChanged()
        }
    }

    override fun loadDataFailure(page: Int, msg: String) {
        shortToast(msg)
    }

    override fun onDestroyView() {
        cureAdapter.destroy()
        curePresenter.destroy()
        super.onDestroyView()
    }

    override fun onLazyActivityCreate() {
        loadData()
    }
}
