package com.leftcoding.network;


import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by LingYan on 2017-09-30
 */
public class NetworkControl {
    private static final int DEFAULT_OUT_TIME = 30;
    private OkHttpClient.Builder okHttpClientBuilder;
    private Cache cache;

    private NetworkControl() {
        okHttpClientBuilder = new OkHttpClient.Builder();
    }

    @SuppressWarnings("unchecked")
    public NetworkControl connectTimeout(long timeout, TimeUnit unit) {
        okHttpClientBuilder.connectTimeout(timeout, unit);
        return this;
    }

    public NetworkControl addNetworkInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    public NetworkControl addInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    public NetworkControl cache(Cache cache) {
        this.cache = cache;
        return this;
    }

    public OkHttpClient build() {
        return okHttpClientBuilder.cache(cache)
                .connectTimeout(DEFAULT_OUT_TIME, TimeUnit.SECONDS)
                .build();
    }

    private static class SingletonHolder {
        private static final NetworkControl INSTANCE = new NetworkControl();
    }

    static synchronized NetworkControl get() {
        return SingletonHolder.INSTANCE;
    }
}
