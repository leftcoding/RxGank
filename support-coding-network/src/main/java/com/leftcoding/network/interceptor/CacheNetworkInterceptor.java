package com.leftcoding.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static com.leftcoding.network.http.HttpConstants.CACHE_CONTROL;
import static com.leftcoding.network.http.HttpConstants.PRAGMA;

/**
 * Create by LingYan on 2019-04-26
 */
public class CacheNetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request())
                .newBuilder()
                .removeHeader(PRAGMA)
                .addHeader(CACHE_CONTROL, "max-age=60")
                .build();
    }
}
