package com.left.gank.base;

import android.ui.base.BaseView;

import com.left.gank.domain.CheckVersion;

/**
 * Create by LingYan on 2016-06-01
 */
public interface ILauncher extends BaseView {
    void callUpdate(CheckVersion checkVersion);
}
