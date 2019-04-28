package android.ly.business.observer;

import android.ly.business.domain.BaseEntity;

import com.leftcoding.rxbus.ObserverListener;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2019-04-27
 */
public abstract class ManagerObserver<T extends BaseEntity> extends ResponseObserver<T> {
    private final String requestTag;
    private final ObserverListener observerListener;

    public ManagerObserver(String requestTag, ObserverListener observerListener) {
        this.requestTag = requestTag;
        this.observerListener = observerListener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (observerListener != null) {
            observerListener.addDisposable(requestTag, d);
        }
    }
}
