package com.leftcoding.network;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class Server extends ServerDisposable implements Builder {
    private Builder builder;

    public Server(Context context) {
        this(context, new ServerBuilder());
    }

    public Server(Context context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    public abstract Server create();

    protected void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Builder initConfig() {
        return builder;
    }

    @Override
    public Builder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    @Override
    public Builder addConverterFactory(Converter.Factory factory) {
        builder.addConverterFactory(factory);
        return this;
    }

    @Override
    public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
        builder.addCallAdapterFactory(factory);
        return this;
    }

    @Override
    public Builder connectTimeOut(int outTime) {
        builder.connectTimeOut(outTime);
        return this;
    }

    @Override
    public Builder cache(Cache cache) {
        builder.cache(cache);
        return this;
    }

    @Override
    public Builder baseUrl(String baseUrl) {
        builder.baseUrl(baseUrl);
        return this;
    }

    @Override
    public Builder client(OkHttpClient client) {
        builder.client(client);
        return this;
    }

    @Override
    public Retrofit retrofit() {
        return builder.retrofit();
    }
}
