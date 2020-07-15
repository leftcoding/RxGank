package android.jsoup;

/**
 * Create by LingYan on 2018-03-23
 */

public class JsoupServer {
    public static final String PC_AGENT = "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36";

    private JsoupServer() {
    }

    public static JsoupMate connect(String url) {
        return new JsoupMate(url);
    }

    public static RxJsoupMate rxConnect(String url) {
        RxJsoupMate rxJsoupMate = new RxJsoupMate(url);
        rxJsoupMate.setUserAgent(PC_AGENT);
        return rxJsoupMate;
    }
}
