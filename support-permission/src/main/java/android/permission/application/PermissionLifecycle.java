package android.permission.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * Create by LingYan on 2019-07-02
 */
public class PermissionLifecycle {
    private static Source source;

    private PermissionLifecycle() {

    }

    public static void permissionApp(Application application, final Callback callback) {
        parsePermissionApp(application);

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (isHitActivity(activity) || isHitFragment(activity)) {
                    if (callback != null) {
                        callback.onActivityStarted(activity);
                    }
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

    /**
     * 是否命中目标 Activity
     *
     * @param sourceActivity 目标 Activity
     * @return true 命中目标，false 未命中目标
     */
    private static boolean isHitActivity(Activity sourceActivity) {
        if (source != null) {
            final String sourceName = sourceActivity.getClass().getCanonicalName();
            final String name = source.activity.getClass().getCanonicalName();
            return TextUtils.equals(sourceName, name);
        }
        return false;
    }

    /**
     * 是否命中目标 Fragment
     *
     * @param sourceActivity 目标 Activity
     * @return true 命中目标，false 未命中目标
     */
    private static boolean isHitFragment(Activity sourceActivity) {
        if (sourceActivity instanceof AppCompatActivity && source != null && source.fragment != null) {
            try {
                final AppCompatActivity appCompatActivity = (AppCompatActivity) sourceActivity;
                final FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                final List<Fragment> fragments = fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment == null) continue;
                    final String sourceName = fragment.getClass().getCanonicalName();
                    final String name = source.fragment.getClass().getCanonicalName();
                    return TextUtils.equals(sourceName, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void parsePermissionApp(Application application) {
        final List<String> clazzNames = PackageUtil.getClassName(application);
        try {
            for (String clazzName : clazzNames) {
                Class c = Class.forName(clazzName);
                boolean isAnnotationPresent = c.isAnnotationPresent(PermissionApp.class);
                if (isAnnotationPresent) {
                    Object o = c.newInstance();
                    if (o instanceof Activity) {
                        Activity activity = (Activity) o;
                        if (source == null) {
                            source = new Source(activity);
                            return;
                        }
                    } else if (o instanceof Fragment) {
                        Fragment fragment = (Fragment) o;
                        if (source == null) {
                            source = new Source(fragment);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
