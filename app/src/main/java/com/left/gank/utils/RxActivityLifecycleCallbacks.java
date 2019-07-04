package com.left.gank.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Create by LingYan on 2019-07-02
 */
public class RxActivityLifecycleCallbacks {
    private RxActivityLifecycleCallbacks() {

    }

    public static void permissionApp(Application application, Callback callback) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (callback != null) {
                    callback.onActivityStarted(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public interface Callback {
        void onActivityStarted(Activity activity);
    }
}
