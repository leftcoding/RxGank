package com.leftcoding.network.interceptor;

import android.content.Context;

import com.leftcoding.network.http.HttpConstants;
import com.leftcoding.network.utils.NetWorkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络，使用缓存数据
 * 没有网络，拦截器不会执行到 networkInterceptor。
 * 执行到 retrofit 的 CacheInterceptor ，就被返回了。所以无网络使用缓存。要写在系统拦截器前。
 * header 中 CACHE_CONTROL 一定要设置 max-stale数值，否则缓存不起作用。
 * Create by LingYan on 2019-04-25
 */
public class CacheOffLineInterceptor implements Interceptor {
    private Context context;

    public CacheOffLineInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            request = request.newBuilder()
                    .removeHeader(HttpConstants.PRAGMA)
                    .header(HttpConstants.CACHE_CONTROL, "public, max-stale=2147483647, only-if-cached")
                    .build();
        }
        return chain.proceed(request);
    }
}