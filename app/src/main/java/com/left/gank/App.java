package com.left.gank;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.coding.file.FilePathUtils;
import com.left.gank.config.Constants;
import com.left.gank.config.HttpUrlConfig;
import com.left.gank.ui.splash.SplashActivity;
import com.left.gank.utils.RxActivityLifecycleCallbacks;
import com.leftcoding.network.HttpServer;
import com.leftcoding.network.interceptor.CacheNetworkInterceptor;
import com.leftcoding.network.interceptor.CacheOffLineInterceptor;
import com.start.permission.RequestCallback;
import com.start.permission.RequestExecutor;
import com.start.permission.Runnable;
import com.start.permission.RxPermission;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.List;

import okhttp3.Cache;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2016-04-01
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RxActivityLifecycleCallbacks.permissionApp(this, new RxActivityLifecycleCallbacks.Callback() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof SplashActivity) {
                    permission(activity);
                }
            }
        });
    }

    /**
     * Bugly 测试
     */
    private void setupBugly() {
        Beta.autoDownloadOnWifi = true;
        Bugly.init(App.this, Constants.CRASH_LOG_ID, false);
    }

    private void permission(Activity activity) {
        RxPermission.with(activity)
                .runtime()
                .checkPermission()
                .rationale(new Runnable() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        permission(activity);
                    }
                })
                .setCallback(new RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                        initSdk();
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        RxPermission.with(activity).launcher().start(100);
                    }
                })
                .start();
    }

    private void initSdk() {
        FilePathUtils.init("edu");
        HttpServer.initConfig()
                .baseUrl(HttpUrlConfig.GANK_URL)
                .cache(new Cache(FilePathUtils.mkHttpPath(), 10 * 1024 * 1024))
                .addInterceptor(new CacheOffLineInterceptor(this))
                .addNetworkInterceptor(new CacheNetworkInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));


        setupBugly();
    }
}
