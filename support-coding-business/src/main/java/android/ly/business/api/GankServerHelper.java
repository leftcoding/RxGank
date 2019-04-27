package android.ly.business.api;

import android.content.Context;
import android.util.Log;

import com.leftcoding.network.ServerBuilder;

import okhttp3.Cache;
import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2018-03-20
 */

public class GankServerHelper {
    private volatile static GankServerHelper gankServerHelper;
    private Context context;
    private ServerBuilder serverBuilder;
    private String baseUrl;

    private GankServerHelper(Context context) {
        this.context = context.getApplicationContext() == null ? context : context.getApplicationContext();
        serverBuilder = new ServerBuilder();
    }

    public static GankServerHelper init(Context context) {
        if (gankServerHelper == null) {
            synchronized (GankServerHelper.class) {
                if (gankServerHelper == null) {
                    gankServerHelper = new GankServerHelper(context);
                }
            }
        }
        return gankServerHelper;
    }

    public GankServerHelper baseUrl(String baseUrl) {
        Log.d(">>", ">>GankServerHelper baseUrl:" + baseUrl);
        this.baseUrl = baseUrl;
        return this;
    }

    public GankServerHelper addInterceptor(Interceptor interceptor) {
        serverBuilder.addInterceptor(interceptor);
        return this;
    }

    public GankServerHelper addNetworkInterceptor(Interceptor interceptor) {
        serverBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    public GankServerHelper cache(Cache cache) {
        serverBuilder.cache(cache);
        return this;
    }

    Retrofit newRetrofit() {
        Log.d(">>>", ">>baseUrl:" + baseUrl);
        return serverBuilder.baseUrl(baseUrl)
                .retrofit();
    }
}
