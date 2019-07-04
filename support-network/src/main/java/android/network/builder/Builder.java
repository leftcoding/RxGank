package android.network.builder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * Create by LingYan on 2019-05-07
 */
public interface Builder {

    public Builder cache(File directory, long maxSize);

    public Builder cache(Cache cache);

    public Builder baseUrl(String baseUrl);

    public Builder addInterceptor(Interceptor interceptor);

    public Builder addNetworkInterceptor(Interceptor interceptor);

    public Builder addConverterFactory(Converter.Factory factory);

    public Builder addCallAdapterFactory(CallAdapter.Factory factory);

    public Builder connectTimeOut(int timeout);
}
