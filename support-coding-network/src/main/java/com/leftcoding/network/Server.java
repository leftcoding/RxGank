package com.leftcoding.network;

import android.content.Context;

import okhttp3.CacheControl;

/**
 * Create by LingYan on 2018-09-21
 */

public abstract class Server extends ServerDisposable {

    protected Server(Context context) {
        super(context);
    }

    public abstract void init(Context context);

    protected CacheControl cacheControl(boolean refresh) {
        return refresh ? CacheControl.FORCE_NETWORK : null;
    }
}
