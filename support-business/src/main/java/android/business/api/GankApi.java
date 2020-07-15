package android.business.api;

import android.business.domain.PageEntity;
import android.business.domain.Solid;

import io.reactivex.Observable;
import okhttp3.CacheControl;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import static android.network.http.constants.HttpConstants.CACHE_CONTROL;


/**
 * Create by LingYan on 2017-09-29
 */

public interface GankApi {
    /**
     * android 资源
     */
    @GET("Android/{limit}/{page}")
    Observable<Response<PageEntity<Solid>>> androids(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * ios 资源
     */
    @GET("iOS/{limit}/{page}")
    Observable<Response<PageEntity<Solid>>> ios(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * 福利 - 图片
     */
    @GET("福利/{limit}/{page}")
    Observable<Response<PageEntity<Solid>>> images(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<PageEntity<Solid>>> videos(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );
}
