package com.left.gank.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by LingYan on 2019-07-02
 */
public class RxActivityLifecycleCallbacks {
    private static Map<String, Activity> activityMap = new HashMap<>();

    private RxActivityLifecycleCallbacks() {

    }

    public static void permissionApp(Application application, Callback callback) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activityMap.containsKey(activity.getLocalClassName()))
                    if (callback != null) {
                        callback.onActivityCreated(activity, savedInstanceState);
                    }
            }

            @Override
            public void onActivityStarted(Activity activity) {

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
        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onFourceFront(Activity activity);
    }
}
