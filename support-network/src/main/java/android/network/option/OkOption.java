package android.network.option;

import android.network.builder.Api;
import android.network.builder.ApiImpl;
import android.network.builder.ConfigBuilder;
import android.network.http.Http;
import android.network.http.OkHttp;

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
