package com.left.gank.ui.cure;

import android.business.domain.Gift;
import android.content.Context;
import android.jsoup.JsoupServer;
import android.rxbus.RxApiManager;
import android.text.TextUtils;
import android.ui.logcat.Logcat;

import com.left.gank.utils.CrashUtils;
import com.left.gank.utils.StringUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by LingYan on 2016-10-26
 */

class CurePresenter extends CureContract.Presenter {
    private static final String BASE_URL = "http://www.mzitu.com/xinggan/";
    private final AtomicBoolean destroyFlag = new AtomicBoolean(false);

    CurePresenter(Context context, CureContract.View view) {
        super(context, view);
    }

    @Override
    void loadData(int page) {
        fetchData(page, getUrl(page));
    }

    @Override
    void loadImages(String url) {
        Disposable disposable = JsoupServer.rxConnect(url)
                .build()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isViewLife()) {
                            view.showLoadingDialog();
                        }
                    }
                })
                .doFinally(() -> {
                    if (isViewLife()) {
                        view.hideLoadingDialog();
                    }
                })
                .subscribe(document -> {
                    getDetailMaxPage(document);
                    List<Gift> list = getImagesList(document);
                    if (isViewLife()) {
                        view.loadImagesSuccess(list);
                    }
                }, e -> {
                    if (isViewLife()) {
                        view.loadImagesFailure(errorTip);
                    }
                });
        RxApiManager.get().add(requestTag, disposable);
    }

    private void fetchData(int page, String url) {
        if (destroyFlag.get()) {
            return;
        }

        Disposable disposable = JsoupServer.rxConnect(url).build()
                .doOnSubscribe(dis -> {
                    if (view != null) {
                        view.showProgress();
                    }
                })
                .doFinally(() -> {
                    if (view != null) {
                        view.hideProgress();
                    }
                })
                .subscribe(document -> {
                    int maxPageNumber = getMaxPageNum(document);
                    List<Gift> gifts = getPageLists(document);
                    if (gifts != null) {
                        if (view != null) {
                            view.loadDataSuccess(page, maxPageNumber, gifts);
                        }
                    }
                }, Logcat::e);
        RxApiManager.get().add(requestTag, disposable);
    }

    /**
     * 解析每面，封面入口地址
     */
    private List<Gift> getPageLists(Document doc) {
        List<Gift> list = new ArrayList<>();
        if (doc == null) {
            return null;
        }
        Elements href = doc.select("#pins > li > a");
        Elements img = doc.select("#pins a img");
        Elements times = doc.select(".time");

        final int countSize = href.size();
        final int imgSize = img.size();
        final int size = countSize > imgSize ? imgSize : countSize;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String imgUrl = img.get(i).attr("data-original");
                String title = img.get(i).attr("alt");
                String url = href.get(i).attr("href");
                String time = times.get(i).text();
                if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(url)) {
                    list.add(new Gift(imgUrl, url, time, title));
                }
            }
        }
        return list;
    }

    private int getMaxPageNum(Document doc) {
        int p = 0;
        if (doc != null) {
            Elements count = doc.select(".nav-links a[href]");
            int size = count.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    String num = count.get(i).text();
                    if (StringUtils.isNumeric(num)) {
                        try {
                            return Integer.parseInt(num);
                        } catch (IllegalFormatException e) {
                            Logcat.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    private int getDetailMaxPage(Document doc) {
        if (doc != null) {
            Elements pages = doc.select(".pagenavi span");
            if (pages != null && !pages.isEmpty()) {

            }
        }
        return 0;
    }

    private List<Gift> getImagesList(Document doc) {
        List<Gift> imagesList = new ArrayList<>();
        if (doc != null) {
            Elements images = doc.select(".main-image a img");
            Elements lengths = doc.select(".pagenavi a");
            String spanLength = null;
            if (lengths != null && lengths.size() > 0) {
                Element element = lengths.get(lengths.size() - 2);
                if (element != null) {
                    spanLength = element.select("span").get(0).html();
                }
            }
            int length;
            try {
                length = spanLength == null ? -1 : Integer.valueOf(spanLength);
            } catch (IllegalFormatException e) {
                length = -1;
            }
            if (images != null && images.size() > 0) {
                String src = images.get(0).attr("src");
                if (!TextUtils.isEmpty(src)) {
                    int index = src.lastIndexOf(".");
                    if (index > 0) {
                        String start = src.substring(0, index);
                        start = start.substring(0, start.length() - 1);
                        String type = src.substring(index);
                        String sub = start.substring(0, start.length() - 1);
                        if (length != -1) {
                            for (int i = 1; i < length; i++) {
                                String page = i < 10 ? String.format(Locale.SIMPLIFIED_CHINESE, "0%d", i) : String.valueOf(i);
                                final String url = sub + page + type;
                                imagesList.add(new Gift(url));
                            }
                        }
                    }
                }
            }
        }
        return imagesList;
    }

    private String getUrl(int page) {
        return page == 1 ? BASE_URL : BASE_URL + "page/" + page;
    }

    @Override
    public void destroy() {
        if (destroyFlag.compareAndSet(false, true)) {
            RxApiManager.get().remove(requestTag);
        }
        super.destroy();
    }

    @Override
    protected void onDestroy() {

    }
}
