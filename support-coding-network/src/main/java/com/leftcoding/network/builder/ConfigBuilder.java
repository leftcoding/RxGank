package com.leftcoding.network.builder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2019-05-07
 */
public class ConfigBuilder implements Builder, HttpBuilder {
    private volatile static ConfigBuilder configBuilder;

    private OkHttpClient.Builder okHttpBuilder;
    private Retrofit.Builder retrofitBuilder;

    private ConfigBuilder() {
        okHttpBuilder = new OkHttpClient.Builder();
        retrofitBuilder = new Retrofit.Builder();
    }

    public static ConfigBuilder init() {
        if (configBuilder == null) {
            synchronized (ConfigBuilder.class) {
                if (configBuilder == null) {
                    configBuilder = new ConfigBuilder();
                }
            }
        }
        return configBuilder;
    }

    @Override
    public Builder cache(File directory, long maxSize) {
        okHttpBuilder.cache(new Cache(directory, maxSize));
        return this;
    }

    @Override
    public Builder cache(Cache cache) {
        okHttpBuilder.cache(cache);
        return this;
    }

    @Override
    public Builder connectTimeOut(int timeout) {
        okHttpBuilder.connectTimeout(timeout, TimeUnit.SECONDS);
        return this;
    }

    @Override
    public Builder addInterceptor(Interceptor interceptor) {
        okHttpBuilder.addInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder addNetworkInterceptor(Interceptor interceptor) {
        okHttpBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder baseUrl(String baseUrl) {
        retrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    @Override
    public Builder addConverterFactory(Converter.Factory factory) {
        configBuilder.addConverterFactory(factory);
        return this;
    }

    @Override
    public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
        configBuilder.addCallAdapterFactory(factory);
        return this;
    }


    @Override
    public OkHttpClient.Builder okHttpBuilder() {
        return okHttpBuilder;
    }

    @Override
    public Retrofit.Builder retrofitBuilder() {
        return retrofitBuilder;
    }
}
