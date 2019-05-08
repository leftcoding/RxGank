package com.leftcoding.network.option;

import com.leftcoding.network.builder.Api;
import com.leftcoding.network.builder.ApiImpl;
import com.leftcoding.network.builder.ConfigBuilder;
import com.leftcoding.network.http.Http;
import com.leftcoding.network.http.OkHttp;

/**
 * Create by LingYan on 2019-05-07
 */
public class OkOption implements Option {

    @Override
    public Api api() {
        Http http = new OkHttp(ConfigBuilder.init());
        return new ApiImpl(http);
    }
}
