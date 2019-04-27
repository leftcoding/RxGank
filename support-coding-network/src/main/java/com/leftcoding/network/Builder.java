package com.leftcoding.network;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2019-04-26
 */
public interface Builder {
    public Builder addInterceptor(Interceptor interceptor);

    public Builder addNetworkInterceptor(Interceptor interceptor);

    public Builder addConverterFactory(Converter.Factory factory);

    public Builder addCallAdapterFactory(CallAdapter.Factory factory);

    public Builder connectTimeOut(int outTime);

    public Builder cache(Cache cache);

    public Builder baseUrl(String baseUrl);

    public Builder client(OkHttpClient client);

    public Retrofit retrofit();
}
