package com.leftcoding.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2017-09-29
 */
public class ServerBuilder implements Builder {
    // 默认超时时间
    private static final int DEFAULT_OUT_TIME = 30;

    private String baseUrl;
    private OkHttpClient.Builder okHttpClientBuilder;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient okHttpClient;

    private int outTime = DEFAULT_OUT_TIME;

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public ServerBuilder() {
        this(new OkHttpClient.Builder(), new Retrofit.Builder());
    }

    private ServerBuilder(OkHttpClient.Builder okHttpClientBuilder, Retrofit.Builder retrofitBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        this.retrofitBuilder = retrofitBuilder;
    }

    @Override
    public Builder addInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder addNetworkInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder addConverterFactory(Converter.Factory factory) {
        retrofitBuilder.addConverterFactory(factory);
        return this;
    }

    @Override
    public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
        retrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    @Override
    public Builder connectTimeOut(int outTime) {
        this.outTime = outTime;
        return this;
    }

    @Override
    public Builder cache(Cache cache) {
        okHttpClientBuilder.cache(cache);
        return this;
    }

    @Override
    public Builder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public Builder client(OkHttpClient client) {
        okHttpClient = client;
        return this;
    }

    private OkHttpClient getClient() {
        return okHttpClient != null
                ? okHttpClient
                : okHttpClientBuilder.connectTimeout(outTime, TimeUnit.SECONDS).build();
    }

    @Override
    public Retrofit retrofit() {
        return retrofitBuilder
                .baseUrl(baseUrl)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
