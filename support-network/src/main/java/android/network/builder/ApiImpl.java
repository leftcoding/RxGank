package android.network.builder;

import android.network.http.Http;

/**
 * Create by LingYan on 2019-05-07
 */
public class ApiImpl implements Api {
    private Http http;

    public ApiImpl(Http http) {
        this.http = http;
    }

    @Override
    public <T> T create(final Class<T> t) {
        return http.builder().create(t);
    }
}
