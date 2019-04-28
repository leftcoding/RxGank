package android.ly.business.api;

import android.ly.business.domain.Gank;
import android.ly.business.domain.ListEntity;
import android.ly.business.domain.PageEntity;

import io.reactivex.Observable;
import okhttp3.CacheControl;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import static com.leftcoding.network.http.HttpConstants.CACHE_CONTROL;


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

    @GET("all/{limit}/{page}")
    Observable<Response<ListEntity<Gank>>> allGoods(
            @Path("page") int page,
            @Path("limit") int limit
    );

    /**
     * 福利 - 图片
     */
    @GET("福利/{limit}/{page}")
    Observable<Response<PageEntity<Gank>>> images(
            @Path("page") int page,
            @Path("limit") int limit
    );

    @GET("休息视频/{limit}/{page}")
    Observable<Response<ListEntity<Gank>>> videos(
            @Path("page") int page,
            @Path("limit") int limit
    );
}
