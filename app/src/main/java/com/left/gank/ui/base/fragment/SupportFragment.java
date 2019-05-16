package com.left.gank.ui.base.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.left.gank.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class SupportFragment extends ButterKnifeFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void shortToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            ToastUtils.showToast(getContext(), str);
        }
    }
}
