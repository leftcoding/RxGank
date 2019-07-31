package android.business.domain;

import java.io.Serializable;

/**
 * Create by LingYan on 2016-07-05
 */
public class Girl implements Serializable {
    /**
     * 月份
     */
    public String month;

    /**
     * 日期
     */
    public String day;

    /**
     * 请求地址
     */
    public String url;

    /**
     * 标题
     */
    public String title;

    public Girl(String url, String title) {
        this.url = url;
        this.title = title;
    }
}
