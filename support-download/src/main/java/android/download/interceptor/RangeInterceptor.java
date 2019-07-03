package android.download.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by LingYan on 2019-05-18
 */
public class RangeInterceptor implements Interceptor {
    private final long startPoint;

    public RangeInterceptor(long startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request.newBuilder()
                .header("RANGE", "bytes=" + startPoint + "-")
                .build());
        if (response.code() == 206) {
            return response;
        }
        return chain.proceed(request);
    }
}
