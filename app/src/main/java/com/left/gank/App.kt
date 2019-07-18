package com.left.gank

import android.app.Application
import android.file.LiFile
import android.network.HttpServer
import android.network.interceptor.CacheNetworkInterceptor
import android.network.interceptor.CacheOffLineInterceptor
import android.permission.Permissions
import android.permission.RequestCallback
import android.permission.application.RxLifecycleCallbacks
import com.left.gank.config.Constants
import com.left.gank.config.HttpUrlConfig
import com.left.gank.domain.PoseCode
import com.left.gank.domain.PoseEvent
import com.left.gank.utils.Permission
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus

/**
 * Create by LingYan on 2016-04-01
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        RxLifecycleCallbacks.permissionApp(this) { activity ->
            Permission.permissionApp(activity, Permissions.Group.STORAGE)
                    .requestCallback(object : RequestCallback {
                        override fun onGranted(list: List<String>?) {
                            initSdk()
                            EventBus.getDefault().post(PoseEvent(PoseCode.NEED_PERMISSION_SUCCESS))
                        }

                        override fun onDenied(list: List<String>?) {
                            Permission.startPermissionSetting(activity)
                        }
                    })
                    .start()
        }
    }

    private fun initSdk() {
        LiFile.init(this)

        HttpServer.initConfig()
                .baseUrl(HttpUrlConfig.GANK_URL)
                .cache(Cache(LiFile.networkFile(), (10 * 1024 * 1024).toLong()))
                .addInterceptor(CacheOffLineInterceptor(this))
                .addNetworkInterceptor(CacheNetworkInterceptor())
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        initBugly()
    }

    /**
     * Bugly 测试
     */
    private fun initBugly() {
        Beta.autoDownloadOnWifi = true
        Bugly.init(this@App, Constants.CRASH_LOG_ID, false)
    }
}
