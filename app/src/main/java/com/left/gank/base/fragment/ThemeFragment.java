package com.left.gank.base.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.LYRelativeLayoutRipple;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Create by LingYan on 2016-10-21
 */

public abstract class ThemeFragment extends ButterKnifeFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    public void changeThemes() {
//        super.changeThemes();
//        if (swipe_refresh != null) {
//            StyleUtils.changeSwipeRefreshLayout(swipe_refresh);
//        }
//    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setItemSelectBackground(@NonNull List<View> list) {
        if (!ListUtils.isEmpty(list)) {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            final int backgroundResource = typedArray.getResourceId(0, 0);

//            ButterKnife.apply(list, new ButterKnife.Action<View>() {
//                @Override
//                public void apply(@NonNull View view, int index) {
//                    view.setBackgroundResource(backgroundResource);
//                }
//            });
        }
    }

    public void setItemBackground(@NonNull List<LYRelativeLayoutRipple> list) {
        if (!ListUtils.isEmpty(list)) {
            final int backgroundResource = R.attr.lyItemSelectBackground;
//            ButterKnife.apply(list, new ButterKnife.Action<LYRelativeLayoutRipple>() {
//                @Override
//                public void apply(@NonNull LYRelativeLayoutRipple view, int index) {
//                    view.setCustomBackgroundResource(backgroundResource);
//                }
//            });
        }
    }
}
