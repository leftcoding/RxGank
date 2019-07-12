package com.left.gank.ui.mine

import android.content.Intent
import android.download.Download
import android.download.DownloadListener
import android.file.RxFile
import android.os.Bundle
import android.ui.logcat.Logcat
import android.view.View
import com.left.gank.R
import com.left.gank.base.fragment.ButterKnifeFragment
import com.left.gank.ui.more.MoreActivity
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * 我的
 * Create by LingYan on 2016-09-21
 */

class MineFragment : ButterKnifeFragment() {
    private lateinit var download: Download

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
        mine_rl_setting!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_SETTING)
        }
        mine_rl_collect!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_COLLECT)
        }
        mine_rl_browse!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_BROWSE)
        }
        mine_download_group!!.setOnClickListener {
            download.apply {
                if (isDownloading) {
                    cancel()
                }
                download()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        download = Download.Builder()
                .savePath(RxFile.apkFile())
                .url("http://b8.market.mi-img.com/download/AppStore/045ac05c5b03b42730855cfeef96cecc4f61d1143/com.duia.duiaapp.apk")
                .downloadListener(downloadListener)
                .build()
    }

    private fun startActivity(type: Int) {
        Intent(context, MoreActivity::class.java).apply {
            putExtra(MoreActivity.TYPE, type)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        if (::download.isInitialized) {
            download.cancel()
        }
        super.onDestroyView()
    }
}
