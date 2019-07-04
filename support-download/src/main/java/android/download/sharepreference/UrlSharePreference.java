package android.download.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by LingYan on 2019-05-18
 */
public class UrlSharePreference {
    public static String lastModifyTime(Context context, String url) {
        return sharedPreferences(context, url).getString(url, null);
    }

    public static long contentLength(Context context, String url) {
        return sharedPreferences(context, url).getLong(url, 0);
    }

    public static void setLastModifyTime(Context context, String url, String value) {
        sharedPreferences(context, url).edit().putString(url, value).apply();
    }

    public static void setContentLength(Context context, String url, long value) {
        sharedPreferences(context, url).edit().putLong(url, value).apply();
    }

    private static SharedPreferences sharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
