package com.left.gank.ui.mine

import android.content.Intent
import android.download.Download
import android.download.DownloadListener
import android.file.FilePathUtils
import android.os.Bundle
import android.ui.logcat.Logcat
import android.view.View
import butterknife.OnClick
import com.left.gank.R
import com.left.gank.ui.base.fragment.ButterKnifeFragment
import com.left.gank.ui.more.MoreActivity
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * 我的
 * Create by LingYan on 2016-09-21
 */

class MineFragment : ButterKnifeFragment() {
    private var download: Download? = null

    private val downloadListener = object : DownloadListener {
        override fun onSuccess() {
            Logcat.d(">>onDownloadSuccess")
        }

        override fun onProgress(progress: Int) {
            if (progress_bar != null) {
                progress_bar!!.progress = progress
            }
        }

        override fun onFailure(msg: String) {
            Logcat.d(">>onDownloadFailed")
        }
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setTitle(R.string.navigation_mine)
        mine_nested_scroll!!.isNestedScrollingEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        download = Download.Builder()
                .savePath(FilePathUtils.mkApkPath())
                .url("http://b8.market.mi-img.com/download/AppStore/045ac05c5b03b42730855cfeef96cecc4f61d1143/com.duia.duiaapp.apk")
                .downloadListener(downloadListener)
                .build()
    }

    @OnClick(R.id.mine_rl_setting)
    internal fun onSetting() {
        openActivity(MoreActivity.TYPE_SETTING)
    }

    @OnClick(R.id.mine_rl_collect)
    internal fun onCollect() {
        openActivity(MoreActivity.TYPE_COLLECT)
    }

    @OnClick(R.id.mine_rl_browse)
    internal fun onBrowse() {
        openActivity(MoreActivity.TYPE_BROWSE)
    }

    @OnClick(R.id.download)
    internal fun download() {
        if (download != null) {
            if (download!!.isDownloading) {
                download!!.cancel()
            }
            download!!.download()
        }
    }

    private fun openActivity(type: Int) {
        Intent(context, MoreActivity::class.java).apply {
            putExtra(MoreActivity.TYPE, type)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (download != null) {
            download!!.cancel()
        }
    }
}
