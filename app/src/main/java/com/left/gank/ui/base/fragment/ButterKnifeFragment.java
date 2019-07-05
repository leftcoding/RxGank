package com.left.gank.ui.base.fragment;

import android.os.Bundle;
import android.ui.base.BaseFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.left.gank.utils.RxLifecycleUtils;
import com.uber.autodispose.AutoDisposeConverter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by LingYan on 2017-09-28
 */

public abstract class ButterKnifeFragment extends BaseFragment {
    private Unbinder unBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(fragmentLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unBinder = ButterKnife.bind(this, view);
    }

    protected abstract int fragmentLayoutId();

    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        return RxLifecycleUtils.bindLifecycle(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null) {
            unBinder.unbind();
        }
    }
}
