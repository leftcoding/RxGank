package com.left.gank.ui.download

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.left.gank.BuildConfig
import com.left.gank.domain.CheckVersion
import com.left.gank.mvp.base.BasePresenter
import com.left.gank.network.DownloadProgressListener
import com.left.gank.network.api.DownloadApi
import com.left.gank.utils.CrashUtils
import com.left.gank.utils.FileUtils
import com.left.gank.view.ILauncher
import com.socks.library.KLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Create by LingYan on 2016-06-01
 */
class DownloadPresenter(context: Context, view: ILauncher) : BasePresenter<ILauncher>(context, view) {
    private var downloadApi: DownloadApi = DownloadApi()
    private var progressListener: DownloadProgressListener? = null
    private val apkName = "gankly.apk"
    private val mFile = File(Environment.getExternalStorageDirectory()
            .absolutePath + "/GankLy/" + apkName)

    fun setDownloadProgressListener(progressListener: DownloadProgressListener) {
        this.progressListener = progressListener;
    }

    fun checkVersion() {
        view.showDialog()
        downloadApi.checkVersion(object : Observer<CheckVersion> {
            override fun onError(e: Throwable) {
                KLog.e(e)
                CrashUtils.crashReport(e)
                view.hiddenDialog()
            }

            override fun onComplete() {
                view.hiddenDialog()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(checkVersion: CheckVersion) {
                val curVersion = BuildConfig.VERSION_CODE
                if (checkVersion.code > curVersion) {
                    view.callUpdate(checkVersion)
                }
            }
        })
    }

    fun downloadApk() {
        downloadApi.downloadApk({ inputStream ->
            try {
                FileUtils.writeFile(inputStream, mFile)
            } catch (e: IOException) {
                KLog.e(e)
                CrashUtils.crashReport(e)
            }
        }, object : Observer<InputStream> {

            override fun onError(e: Throwable) {
                KLog.e(e)
                CrashUtils.crashReport(e)
            }

            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(inputStream: InputStream) {
                downloadSuccess(context, mFile)
            }
        })
    }

    private fun downloadSuccess(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(Uri.parse("file://" + file.absolutePath), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    override fun onDestroy() {

    }
}
