package com.leftcoding.network;

import android.content.Context;

import okhttp3.CacheControl;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class Server extends BaseServer {
    private Builder builder;
    private Retrofit retrofit;

    public Server(Context context) {
        this(context, new ServerBuilder());
    }

    public Server(Context context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    public Builder initConfig() {
        return builder;
    }

    protected <T> T create(final Class<T> service) {
        if (retrofit == null) {
            retrofit = builder.retrofit();
        }
        return retrofit.create(service);
    }

    protected CacheControl cacheControl(boolean refresh) {
        return refresh ? CacheControl.FORCE_NETWORK : null;
    }

    public abstract void clean(String tag);
}
