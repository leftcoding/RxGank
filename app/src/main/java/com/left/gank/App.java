package com.left.gank;

import android.app.Application;
import android.ly.business.api.GankServer;
import android.os.Environment;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.left.gank.config.Constants;
import com.left.gank.config.HttpUrlConfig;
import com.leftcoding.network.interceptor.CacheNetworkInterceptor;
import com.leftcoding.network.interceptor.CacheOffLineInterceptor;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;

import okhttp3.Cache;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2016-04-01
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupStetho();
        setupX5WebView();
        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy/network");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        GankServer.with()
                .init()
                .baseUrl(HttpUrlConfig.GANK_URL)
                .cache(new Cache(appDir, 10 * 1024 * 1024))
                .addInterceptor(new CacheOffLineInterceptor(this))
                .addNetworkInterceptor(new CacheNetworkInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    /**
     * 数据库Chrome上调试
     */
    private void setupStetho() {
        Stetho.initializeWithDefaults(App.this);
    }

    /**
     * Bugly 测试
     */
    private void setupBugly() {
        Beta.autoDownloadOnWifi = true;
//        Beta.autoCheckUpgrade = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        Bugly.init(App.this, Constants.CRASH_LOG_ID, false);
    }

    /**
     * x5 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
     */
    private void setupX5WebView() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(App.this, cb);
    }
}
