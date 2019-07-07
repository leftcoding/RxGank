package com.left.gank.ui.index

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.domain.CheckVersion
import com.left.gank.network.DownloadProgressListener
import com.left.gank.ui.android.AndroidFragment
import com.left.gank.ui.base.LazyFragment
import com.left.gank.ui.base.fragment.SupportFragment
import com.left.gank.ui.ios.IosFragment
import com.left.gank.ui.welfare.WelfareFragment
import com.left.gank.view.ILauncher
import com.socks.library.KLog
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

/**
 * Create by LingYan on 2016-04-22
 */
class IndexFragment : SupportFragment(), DownloadProgressListener, ILauncher {
    private var titles: MutableList<String>? = null
    private val presenter: LauncherPresenter? = null
    private var progressDialog: ProgressDialog? = null
    private var appLength: Long = 0

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            activity!!.title = titles!![position]
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragments = ArrayList<LazyFragment>()
        fragments.add(AndroidFragment.newInstance())
        fragments.add(IosFragment.newInstance())
        fragments.add(WelfareFragment.newInstance())

        titles = ArrayList()
        titles!!.add(Constants.ANDROID)
        titles!!.add(Constants.IOS)
        titles!!.add(Constants.WELFRAE)

        val mPagerAdapter = IndexPagerAdapter(childFragmentManager, fragments, titles)
        main_view_pager!!.adapter = mPagerAdapter
        main_view_pager!!.offscreenPageLimit = fragments.size
        main_view_pager!!.addOnPageChangeListener(onPageChangeListener)

        for (i in titles!!.indices) {
            tab_layout!!.addTab(tab_layout!!.newTab().setText(titles!![i]))
        }

        tab_layout!!.setupWithViewPager(main_view_pager)
        tab_layout!!.tabMode = TabLayout.MODE_FIXED
        tab_layout!!.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        presenter = new LauncherPresenter(getActivity(), this, this);
    }

    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
        KLog.d("bytesRead:$bytesRead,contentLength:$contentLength,done:$done")
        if (bytesRead > 0 && progressDialog != null && appLength > 0) {
            progressDialog!!.progress = bytesRead.toInt()
        }

        if (done) {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }

    override fun callUpdate(checkVersion: CheckVersion) {
        appLength = checkVersion.appLength
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(checkVersion.changelog)
        builder.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        builder.setPositiveButton("更新") { dialog, which ->
            presenter!!.downloadApk()
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
                progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressDialog!!.setCancelable(true)
                progressDialog!!.setCanceledOnTouchOutside(false)
            }
            progressDialog!!.setMessage("更新中...")
            progressDialog!!.max = appLength.toInt()
            progressDialog!!.show()
        }
        builder.show()
    }

    override fun showDialog() {}

    override fun noNewVersion() {

    }

    override fun hiddenDialog() {}
}
