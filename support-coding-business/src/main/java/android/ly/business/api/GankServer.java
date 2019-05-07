package android.ly.business.api;

import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.RxServer;
import com.leftcoding.network.BaseServer;
import com.leftcoding.network.builder.Builder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer extends BaseServer {
    private volatile static GankServer server;
    private GankApi gankApi;

    private GankServer() {
        super();
    }

    @Override
    public Builder init() {
        return RxServer.initConfig();
    }

    @Override
    public GankServer api() {
        if (gankApi == null) {
            gankApi = RxServer.get().api().create(GankApi.class);
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
}
