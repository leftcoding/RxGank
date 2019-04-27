package android.ly.business.observer;

import com.leftcoding.rxbus.RxApiManager;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2019-04-27
 */
public abstract class ManagerObserver<T> extends BaseObserver<T> {
    private String observerTag;

    ManagerObserver(String requestTag) {
        this.observerTag = requestTag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        RxApiManager.get().add(observerTag, d);
    }
}
