package com.left.gank.ui.pure

import android.app.ProgressDialog
import android.business.domain.Gift
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.left.gank.R
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.gallery.GalleryActivity
import com.left.gank.utils.ListUtils
import com.left.gank.widget.LySwipeRefreshLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_gift.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 妹子 - 清纯
 * Create by LingYan on 2016-05-17
 */
class PureFragment : LazyFragment(), PureContract.View {
    private lateinit var pureAdapter: PureAdapter
    private lateinit var purePresenter: PureContract.Presenter
    private var progressDialog: ProgressDialog? = null
    private var disposable: Disposable? = null
    private var curPage = FIRST_PAGE

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_gift
    }

    private val listener = object : LySwipeRefreshLayout.OnListener {

        override fun onRefresh() {
            curPage = FIRST_PAGE
            loadData()
        }

        override fun onLoadMore() {
            loadData()
        }
    }

    private val pureCallback = object : PureAdapter.Callback {
        override fun onItemClick(gift: Gift) {
            disposable = Observable.just(gift)
                    .throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { gift1 -> if (gift1.url == null) "" else gift1.url }
                    .subscribe { url ->
                        purePresenter.loadImages(url)
                    }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pureAdapter = PureAdapter()
        swipe_refresh!!.setAdapter(pureAdapter)
        swipe_refresh!!.setLayoutManager(StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL))
        swipe_refresh!!.setOnScrollListener(listener)
        pureAdapter.setOnItemClickListener(pureCallback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        purePresenter = PurePresenter(context!!, this)
        loadData()
    }

    private fun loadData() {
        purePresenter.loadData(curPage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showProgress() {
        if (swipe_refresh != null) {
            swipe_refresh!!.isRefreshing = true
        }
    }

    override fun hideProgress() {
        if (swipe_refresh != null) {
            swipe_refresh!!.isRefreshing = false
        }
        if (multiple_status_view != null) {
            multiple_status_view!!.showContent()
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

    override fun onDestroyView() {
        pureAdapter.destroy()
        if (disposable != null) {
            disposable!!.dispose()
        }
        super.onDestroyView()
    }

    override fun onLazyActivityCreate() {

    }

    override fun loadDataSuccess(page: Int, list: List<Gift>) {
        if (ListUtils.isNotEmpty(list)) {
            curPage = page + 1
            if (page == FIRST_PAGE) {
                pureAdapter.refillItems(list)
            } else {
                pureAdapter.appendItems(list)
            }
            pureAdapter.notifyDataSetChanged()
        }
    }

    override fun loadDataFailure(page: Int, msg: String) {
        shortToast(msg)
    }

    override fun showLoadingDialog() {
        hideLoadingDialog()
        progressDialog = ProgressDialog(context)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage(context!!.getString(R.string.loading_meizi_images))
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCanceledOnTouchOutside(true)
        progressDialog!!.setOnCancelListener { dialog -> purePresenter!!.destroy() }

        if (!progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    override fun hideLoadingDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    override fun loadImagesSuccess(list: List<Gift>) {
        val bundle = Bundle()
        val intent = Intent(context, GalleryActivity::class.java)
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_GIFT)
        intent.putExtra(GalleryActivity.EXTRA_LIST, list as ArrayList<*>)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun loadImagesFailure(msg: String) {
        shortToast(msg)
    }

    companion object {
        private const val FIRST_PAGE = 1

        val instance: PureFragment
            get() = PureFragment()
    }
}
