package android.business.api;

import android.business.domain.Gank;
import android.business.domain.PageEntity;
import android.network.HttpServer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer extends HttpServer {
    private volatile static GankServer server;
    private GankApi gankApi;

    private GankServer() {
        super();
    }

    public GankServer api() {
        if (gankApi == null) {
            gankApi = HttpServer.get().api().create(GankApi.class);
        }
        return this;
    }

    public static GankServer with() {
        if (server == null) {
            synchronized (GankServer.class) {
                if (server == null) {
                    server = new GankServer();
                }
            }
        }
        return server;
    }

    public Observable<Response<PageEntity<Gank>>> androids(final boolean refresh, final int page, final int limit) {
        return gankApi.androids(cacheControl(refresh), page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<PageEntity<Gank>>> ios(final boolean refresh, final int page, final int limit) {
        return gankApi.ios(cacheControl(refresh), page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<PageEntity<Gank>>> images(final boolean refresh, final int page, final int limit) {
        return gankApi.images(cacheControl(refresh), page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<PageEntity<Gank>>> video(final boolean refresh, final int page, final int limit) {
        return gankApi.videos(cacheControl(refresh), page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
