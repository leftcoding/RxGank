package android.ly.business.observer;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-10-15
 */

public abstract class BaseObserver<T> implements Observer<T> {
    BaseObserver() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
    }
}
