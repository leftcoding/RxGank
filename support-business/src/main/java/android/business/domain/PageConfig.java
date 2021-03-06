package android.business.domain;

/**
 * Create by LingYan on 2017-11-18
 */

public class PageConfig {
    /**
     * 分页个数 默认20
     */
    public int limit = 20;

    /**
     * 初始化请求页数
     */
    public int initPage = 1;

    /**
     * 当前请求页数
     */
    private int curPage = 1;

    private boolean isEnd;

    public PageConfig() {
    }

    public PageConfig(int limit, int initPage, int curPage) {
        this.limit = limit;
        this.initPage = initPage;
        this.curPage = curPage;
    }

    public boolean isFirst() {
        return initPage == curPage;
    }

    public int getNextPage() {
        return curPage + 1;
    }

    public void setCurPage(int page) {
        if (page == curPage && page > 1) {
            isEnd = true;
        }
        this.curPage = page;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getInitPage() {
        return initPage;
    }

    public static int starPage() {
        return 1;
    }

    public static boolean isFirstPage(int page) {
        return starPage() >= page;
    }
}
