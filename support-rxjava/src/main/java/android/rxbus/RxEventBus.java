package android.rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Create by LingYan on 2016-12-15
 */

public class RxEventBus {
    private volatile static RxEventBus rxEventBus;
    private final Subject<Object> eventBus;

    private final Map<Class<?>, Object> stickyEventMap;

    private RxEventBus() {
        eventBus = PublishSubject.create().toSerialized();
        stickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxEventBus newInstance() {
        if (rxEventBus == null) {
            synchronized (RxEventBus.class) {
                if (rxEventBus == null) {
                    rxEventBus = new RxEventBus();
                }
            }
        }
        return rxEventBus;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        eventBus.onNext(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return eventBus.ofType(eventType);
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return eventBus.hasObservers();
    }

    public void reset() {
        rxEventBus = null;
    }


    // Sticky 相关

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (stickyEventMap) {
            Observable<T> observable = eventBus.ofType(eventType);
            final Object event = stickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEventMap) {
            stickyEventMap.clear();
        }
    }
}
