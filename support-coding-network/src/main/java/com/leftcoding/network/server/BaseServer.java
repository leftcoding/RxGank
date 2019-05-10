package com.leftcoding.network.server;

import com.leftcoding.network.builder.Builder;

import okhttp3.CacheControl;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class BaseServer extends Server {

    public BaseServer() {
    }

    public abstract Builder init();

    public abstract BaseServer api();

    protected CacheControl cacheControl(boolean refresh) {
        return refresh ? CacheControl.FORCE_NETWORK : null;
    }
}
