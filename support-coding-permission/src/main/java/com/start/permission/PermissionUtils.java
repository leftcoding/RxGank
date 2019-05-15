package com.start.permission;

import android.os.Build;

/**
 * Create by LingYan on 2019-05-15
 */
public class PermissionUtils {
    public static boolean isRunTime() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M;
    }
}
