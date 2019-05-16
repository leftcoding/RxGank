package android.ly.business.observer;

import android.ly.business.domain.BaseEntity;

import retrofit2.Response;

/**
 * Create by LingYan on 2017-11-19
 */

public abstract class ResponseObserver<T extends BaseEntity> extends BaseObserver<Response<T>> {

    public ResponseObserver() {
    }

    protected abstract void onSuccess(T entity);

    protected abstract void onFailure(Throwable e);

    @Override
    public void onNext(Response<T> response) {
        if (response == null) {
            onFailure(new Throwable("response is null"));
            return;
        }
        if (!response.isSuccessful()) {
            onFailure(new Throwable("request code is:" + response.code()));
            return;
        }
        onSuccess(response.body());
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
