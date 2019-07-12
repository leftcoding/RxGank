package android.ui.base;

/**
 * Create by LingYan on 2016-10-20
 */

public interface BaseView extends BaseContract.View {
    void showProgress();

    void hideProgress();

    void showEmpty();

    void showContent();
}
