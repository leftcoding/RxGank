package android.rxbus;

import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2019-04-28
 */
public interface ObserverListener {
    /**
     * 添加订阅者监听
     *
     * @param tag        标志
     * @param disposable {@link Disposable}
     */
    void add(String tag, Disposable disposable);

    /**
     * 移除订阅者监听
     *
     * @param tag 标志
     */
    void remove(String tag);

    /**
     * 清除订阅者监听
     */
    void clear();
}
