package com.leftcoding.network;

import com.leftcoding.network.builder.Builder;
import com.leftcoding.network.builder.ConfigBuilder;
import com.leftcoding.network.option.OkOption;
import com.leftcoding.network.option.Option;
import com.leftcoding.network.server.BaseServer;

/**
 * Create by LingYan on 2019-05-07
 */
public class HttpServer extends BaseServer {
    public HttpServer() {
    }

    public static Option get() {
        return new OkOption();
    }

    public static Builder initConfig() {
        return ConfigBuilder.init();
    }
}
