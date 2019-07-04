package android.network.interceptor;

import android.content.Context;
import android.network.http.constants.HttpConstants;
import android.network.utils.NetWorkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络，使用缓存数据
 * 没有网络，拦截器不会执行到 networkInterceptor。
 * 执行到 retrofit 的 CacheInterceptor ，就被返回了。所以无网络使用缓存。要写在系统拦截器前。
 * header 中 CACHE_CONTROL 一定要设置 max-stale数值，否则缓存不起作用。
 * Create by LingYan on 2019-04-25
 */
public class CacheOffLineInterceptor implements Interceptor {
    private Context context;

    public CacheOffLineInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        final boolean isNetworkAvailable = NetWorkUtil.isNetworkAvailable(context);
        final String cacheControl = request.header(HttpConstants.CACHE_CONTROL);
        final boolean isNoCache = cacheControl != null && cacheControl.contains("no-cache");
        if (!isNetworkAvailable || !isNoCache) {
            request = request.newBuilder()
                    .removeHeader(HttpConstants.PRAGMA)
                    .header(HttpConstants.CACHE_CONTROL, "public, max-stale=2147483647, only-if-cached")
                    .build();
        }
        // 防止第一使用缓存，缓存不存在，出现504情况
        Response response = chain.proceed(request);
        if (response != null) {
            String msg = response.message();
            if (response.code() == 504 && msg.contains("only-if-cached")) {
                return chain.proceed(request.newBuilder()
                        .removeHeader(HttpConstants.PRAGMA)
                        .removeHeader(HttpConstants.CACHE_CONTROL)
                        .build());
            }
        }
        return response;
    }
}
