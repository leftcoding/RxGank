package com.leftcoding.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 网络工具类
 */
public class NetWorkUtil {

    /**
     * 判断是否有连接网络
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;

    }

    /**
     * 获取网络类型,-1表示无网络，1表示WIFI网络，2表示wap网络，3表示net网络
     */
    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return netType;
        }

        int nType = info.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (info.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }

        return netType;
    }

    /**
     * 获得连接头的序列化
     */
    public void getHeaderMap(HttpURLConnection conn) {
        Map<String, List<String>> headerMap = conn.getHeaderFields();
        Iterator<String> iterator = headerMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<String> values = headerMap.get(key);
        }
    }


    /**
     * </br><b>title : </b>		网络是否可用
     * </br><b>description :</b>网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return !(networkinfo == null || !networkinfo.isAvailable());
    }

}