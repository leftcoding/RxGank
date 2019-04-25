package com.leftcoding.network.interceptor;

import android.content.Context;

import com.leftcoding.network.utils.NetWorkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by LingYan on 2019-04-25
 */
public class CacheInterceptor implements Interceptor {
    private Context context;

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        boolean netAvailable = NetWorkUtil.isNetworkAvailable(context);
        Response resp;
        Request req;
        if (netAvailable) {
            //有网络,检查10秒内的缓存
            req = chain.request()
                    .newBuilder()
                    .cacheControl(new CacheControl
                            .Builder()
                            .maxAge(10, TimeUnit.SECONDS)
                            .build())
                    .build();
        } else {
            //无网络,检查30天内的缓存,即使是过期的缓存
            req = chain.request().newBuilder()
                    .cacheControl(new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(30, TimeUnit.DAYS)
                            .build())
                    .build();
        }
        resp = chain.proceed(req);
        return resp.newBuilder().build();
    }
}
