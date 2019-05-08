package com.leftcoding.network.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leftcoding.network.builder.HttpBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by LingYan on 2019-05-07
 */
public class OkHttp implements Http {
    private HttpBuilder builder;

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public OkHttp(HttpBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Retrofit builder() {
        return builder.retrofitBuilder()
                .client(builder.okHttpBuilder().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
