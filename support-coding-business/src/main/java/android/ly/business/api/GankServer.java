package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.Server;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer extends Server {
    private volatile static GankServer server;
    private GankApi gankApi;

    private GankServer(Context context) {
        super(context);
    }

    public GankServer api() {
        if (gankApi == null) {
            gankApi = create(GankApi.class);
        }
        return this;
    }

    public static GankServer with(Context context) {
        if (server == null) {
            synchronized (GankServer.class) {
                if (server == null) {
                    server = new GankServer(context);
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
}
