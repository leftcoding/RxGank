package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.Server;
import com.leftcoding.rxbus.RxApiManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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

    @Override
    public void clean(String tag) {
        RxApiManager.get().clean(tag);
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

    public Observable<PageEntity<Gank>> ios(int page, int limit) {
        return gankApi.ios(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<PageEntity<Gank>>, PageEntity<Gank>>() {
                    @Override
                    public PageEntity<Gank> apply(Response<PageEntity<Gank>> pageEntityResponse) throws Exception {
                        if (pageEntityResponse == null) {
                            return null;
                        }
                        return pageEntityResponse.body();
                    }
                });
    }

    public Observable<PageEntity<Gank>> images(final int page, final int limit) {
        return gankApi.images(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<PageEntity<Gank>>, PageEntity<Gank>>() {
                    @Override
                    public PageEntity<Gank> apply(Response<PageEntity<Gank>> response) throws Exception {
                        if (response == null || !response.isSuccessful()) {
                            return null;
                        }
                        return response.body();
                    }
                });
    }
}
