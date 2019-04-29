package com.left.gank.ui.base.activity;

import com.left.gank.mvp.base.SupportView;

/**
 * Create by LingYan on 2017-10-03
 */

public abstract class SupportActivity extends BaseActivity implements SupportView {

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

    @Override
    protected int getContentId() {
        return 0;
    }
}
