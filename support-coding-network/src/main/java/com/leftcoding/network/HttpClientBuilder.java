package com.leftcoding.network;


import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by LingYan on 2017-09-30
 */
public class HttpClientBuilder {
    private static final int DEFAULT_OUT_TIME = 30;
    private OkHttpClient.Builder okHttpClientBuilder;
    private Cache cache;

    private HttpClientBuilder() {
        okHttpClientBuilder = new OkHttpClient.Builder();
    }

    public HttpClientBuilder connectTimeout(long timeout, TimeUnit unit) {
        okHttpClientBuilder.connectTimeout(timeout, unit);
        return this;
    }

    public HttpClientBuilder addNetworkInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    public HttpClientBuilder addInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    public HttpClientBuilder cache(Cache cache) {
        this.cache = cache;
        return this;
    }

    public OkHttpClient build() {
        return okHttpClientBuilder.cache(cache)
                .connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS)
                .build();
    }
}
