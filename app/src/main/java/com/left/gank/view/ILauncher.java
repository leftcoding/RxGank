package com.left.gank.view;

import android.left.ui.base.BaseView;

import com.left.gank.bean.CheckVersion;

/**
 * Create by LingYan on 2016-06-01
 */
public interface ILauncher extends BaseView {
    void callUpdate(CheckVersion checkVersion);

    void showDialog();

    void noNewVersion();

    void hiddenDialog();
}
