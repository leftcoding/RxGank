package android.network.builder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Create by LingYan on 2019-05-07
 */
public interface HttpBuilder {
    OkHttpClient.Builder okHttpBuilder();

    Retrofit.Builder retrofitBuilder();
}
