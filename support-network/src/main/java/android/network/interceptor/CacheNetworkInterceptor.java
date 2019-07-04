package android.network.interceptor;

import android.network.http.constants.HttpConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 进行网络缓存，默认缓存10s
 * Create by LingYan on 2019-04-26
 */
public class CacheNetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originResponse = chain.proceed(chain.request());
        final String cacheControl = originResponse.header(HttpConstants.CACHE_CONTROL);
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originResponse.newBuilder()
                    .removeHeader(HttpConstants.PRAGMA)
                    .header(HttpConstants.CACHE_CONTROL, "public,max-age=10")
                    .build();
        } else {
            return originResponse;
        }
    }
}
