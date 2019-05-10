package com.leftcoding.network.server;

import okhttp3.CacheControl;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class BaseServer extends Server {

    public BaseServer() {
    }

    protected CacheControl cacheControl(boolean refresh) {
        return refresh ? CacheControl.FORCE_NETWORK : null;
    }
}
