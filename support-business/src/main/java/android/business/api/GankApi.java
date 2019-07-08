package android.business.api;

import android.business.domain.Gank;
import android.business.domain.PageEntity;

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
    Observable<Response<PageEntity<Gank>>> androids(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * ios 资源
     */
    @GET("iOS/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> ios(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * 福利 - 图片
     */
    @GET("福利/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> images(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> videos(
            @Header(CACHE_CONTROL) CacheControl cacheControl,
            @Path("page") int page,
            @Path("limit") int limit
    );
}
