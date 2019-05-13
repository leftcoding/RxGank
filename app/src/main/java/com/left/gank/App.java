package com.left.gank;

import android.app.Application;

import com.coding.file.FilePathUtils;
import com.left.gank.config.Constants;
import com.left.gank.config.HttpUrlConfig;
import com.leftcoding.network.HttpServer;
import com.leftcoding.network.interceptor.CacheNetworkInterceptor;
import com.leftcoding.network.interceptor.CacheOffLineInterceptor;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import okhttp3.Cache;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2016-04-01
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FilePathUtils.init("edu");
        HttpServer.initConfig()
                .baseUrl(HttpUrlConfig.GANK_URL)
                .cache(new Cache(FilePathUtils.mkHttpPath(), 10 * 1024 * 1024))
                .addInterceptor(new CacheOffLineInterceptor(this))
                .addNetworkInterceptor(new CacheNetworkInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));


        setupBugly();
    }

    /**
     * Bugly 测试
     */
    private void setupBugly() {
        Beta.autoDownloadOnWifi = true;
        Bugly.init(App.this, Constants.CRASH_LOG_ID, false);
    }
}
