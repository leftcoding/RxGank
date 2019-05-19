package com.left.gank.ui.pure;

import android.content.Context;
import android.lectcoding.ui.logcat.Logcat;
import android.ly.business.domain.Gift;
import android.ly.jsoup.JsoupServer;
import android.text.TextUtils;

import com.left.gank.utils.CrashUtils;
import com.left.gank.utils.ListUtils;
import com.left.gank.utils.StringUtils;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-12-27
 */

public class PurePresenter extends PureContract.Presenter {
    private static String BASE_URL = "http://www.mzitu.com/mm/";
    private static String HOST = "page/";
    private static String nextUrl = BASE_URL + HOST;
    private int maxPageNumber;
    private int maxImages;

    PurePresenter(Context context, PureContract.View view) {
        super(context, view);
    }

    @Override
    void loadData(int page) {
        String url = getUrl(page);
        JsoupServer.rxConnect(url).build()
                .doOnSubscribe(disposable -> {
                    if (view != null) {
                        view.showProgress();
                    }
                })
                .doFinally(() -> view.hideProgress())
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Document document) {
                        maxPageNumber = getMaxPageNum(document);
                        List<Gift> gifts = getPageLists(document);
                        if (gifts != null) {
                            if (view != null) {
                                view.loadDataSuccess(page, gifts);
                            }
                            return;
                        }
                        if (view != null) {
                            view.loadDataFailure(page, errorTip);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logcat.e(e);
                        if (view != null) {
                            view.loadDataFailure(page, errorTip);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    void loadImages(String url) {
        JsoupServer.rxConnect(url)
                .build()
                .doOnSubscribe(disposable -> {
                    if (view != null) {
                        view.showLoadingDialog();
                    }
                })
                .map(document -> {
                    maxImages = getImageMaxPage(document);
                    String firstUrl = getImageFirstUrl(document);
                    return getImages(firstUrl);
                })
                .doFinally(() -> {
                    if (view != null) {
                        view.hideLoadingDialog();
                    }
                })
                .subscribe(new Observer<List<Gift>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Gift> list) {
                        if (ListUtils.isNotEmpty(list)) {
                            if (isViewLife()) {
                                view.loadImagesSuccess(list);
                                return;
                            }
                        }
                        if (isViewLife()) {
                            view.loadImagesFailure(errorTip);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        CrashUtils.crashReport(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private int getImageMaxPage(Document doc) {
        int max = 0;
        if (doc != null) {
            Elements pages = doc.select(".pagenavi a[href]");
            int size = pages.size();
            for (int i = size - 1; i > 0; i--) {
                String page = pages.get(i).text();
                if (StringUtils.isNumeric(page)) {
                    max = Integer.parseInt(page);
                    break;
                }
            }
        }
        return max;
    }

    private String getImageFirstUrl(Document doc) {
        Elements links = doc.select(".main-image img[src$=.jpg]");
        return links.get(0).attr("src");
    }

    private ArrayList<Gift> getImages(String url) {
        ArrayList<Gift> imagesList = new ArrayList<>();
        String baseUrl = null;
        String name = null;
        String endType = null;
        int lastPointIndex;
        int lastNameIndex;
        if (url.contains(".")) {
            if (url.contains("-")) {
                lastPointIndex = url.lastIndexOf("-");
            } else {
                lastPointIndex = url.lastIndexOf(".");
            }
            lastNameIndex = url.lastIndexOf("/");
            baseUrl = url.substring(0, lastNameIndex);
            name = url.substring(lastNameIndex, lastPointIndex - 2);
            endType = url.substring(lastPointIndex);
        }

        String number;
        String lastUrl;
        for (int i = 1; i <= maxPageNumber; i++) {
            if (i < 10) {
                number = "0" + i;
            } else {
                number = String.valueOf(i);
            }
            lastUrl = baseUrl + name + number + endType;
            imagesList.add(new Gift(lastUrl));
        }
        return imagesList;
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

    private String getUrl(int page) {
        String _url;
        if (page == 1) {
            _url = BASE_URL;
        } else {
            _url = nextUrl + page;
        }
        return _url;
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
                            KLog.e(e);
                            CrashUtils.crashReport(e);
                        }
                    }
                }
            }
        }
        return p;
    }

    @Override
    protected void onDestroy() {

    }
}
