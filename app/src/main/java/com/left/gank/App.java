package com.left.gank;

import android.app.Activity;
import android.app.Application;
import android.file.FilePathUtils;
import android.network.HttpServer;
import android.network.interceptor.CacheNetworkInterceptor;
import android.network.interceptor.CacheOffLineInterceptor;
import android.permission.Permissions;
import android.permission.RequestCallback;
import android.rxbus.RxEventBus;

import com.left.gank.config.Constants;
import com.left.gank.config.HttpUrlConfig;
import com.left.gank.data.bean.PoseCode;
import com.left.gank.data.bean.PoseEvent;
import com.left.gank.ui.splash.SplashActivity;
import com.left.gank.utils.Permission;
import com.left.gank.utils.RxActivityLifecycleCallbacks;
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
            public void onActivityStarted(Activity activity) {
                if (activity instanceof SplashActivity) {
                    Permission.initPermissions(activity, new RequestCallback() {
                        @Override
                        public void onGranted(List<String> list) {
                            initSdk();
                            RxEventBus.newInstance().post(new PoseEvent(PoseCode.NEED_PERMISSION_SUCCESS));
                        }

                        @Override
                        public void onDenied(List<String> list) {
                            Permission.startPermissionSetting(activity);
                        }
                    }, Permissions.Group.STORAGE);
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
