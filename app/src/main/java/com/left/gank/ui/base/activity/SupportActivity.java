package com.left.gank.ui.base.activity;

import com.left.gank.mvp.base.SupportView;
import com.left.gank.utils.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * Create by LingYan on 2017-10-03
 */

public abstract class SupportActivity extends BaseActivity implements SupportView {

    @Override
    protected int getContentId() {
        return 0;
    }

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showEmpty() {

    }
}
