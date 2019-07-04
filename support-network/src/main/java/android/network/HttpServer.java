package android.network;

import android.network.builder.Builder;
import android.network.builder.ConfigBuilder;
import android.network.option.OkOption;
import android.network.option.Option;
import android.network.server.BaseServer;

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
