package android.ly.business.observer;

import android.ly.business.domain.BaseEntity;

import retrofit2.Response;

/**
 * Create by LingYan on 2017-11-19
 */

public abstract class ResponseObserver<T extends BaseEntity> extends ManagerObserver<Response<T>> {

    public ResponseObserver(String requestTag) {
        super(requestTag);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure(Throwable e);

    @Override
    public void onNext(Response<T> t) {
        if (t == null) {
            onFailure(new Throwable("response is null"));
            return;
        }
        if (!t.isSuccessful()) {
            onFailure(new Throwable("request code is:" + t.code()));
            return;
        }
        onSuccess(t.body());
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        onFailure(e);
    }

    @Override
    public void onComplete() {
        // nothing
    }
}
