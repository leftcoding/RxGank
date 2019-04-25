package android.ly.business.api;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageEntity;

import com.leftcoding.network.Server;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CacheControl;
import retrofit2.Response;


/**
 * Create by LingYan on 2017-09-30
 */

public class GankServer extends Server {
    private volatile static GankServer server;
    private GankServerHelper gankServerHelper;
    private Api api;

    private GankServer(Context context) {
        super(context);
        if (gankServerHelper == null) {
            gankServerHelper = GankServerHelper.init(context);
        }
        api = gankServerHelper
                .newRetrofit()
                .create(Api.class);
    }

    @Override
    public void init(Context context) {
        gankServerHelper = GankServerHelper.init(context);
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

    public Observable<PageEntity<Gank>> androids(final boolean refresh, final int page, final int limit) {
        return api.androids(cacheControl(refresh), page, limit)
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

    public Observable<PageEntity<Gank>> ios(int page, int limit) {
        return api.ios(page, limit)
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
        return api.images(page, limit)
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
