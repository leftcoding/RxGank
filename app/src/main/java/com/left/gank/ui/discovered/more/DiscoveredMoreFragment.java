package com.left.gank.ui.discovered.more;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.left.gank.R;
import com.left.gank.base.LazyFragment;
import com.left.gank.ui.baisi.BaiSiActivity;
import com.left.gank.utils.theme.ThemeColor;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by LingYan on 2016-12-02
 */

public class DiscoveredMoreFragment extends LazyFragment {
    @BindView(R.id.discovered_parent)
    LinearLayout mLinearLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    protected void callBackRefreshUi() {
        ThemeColor color = new ThemeColor(this);
        color.setBackgroundResource(R.attr.themeBackground, mLinearLayout);
        color.start();
    }

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_discovered_more;
    }

    @OnClick(R.id.discovered_rl_budejie)
    void onClickBuDeJie() {
        getActivity().startActivity(new Intent(getActivity(), BaiSiActivity.class));
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onLazyActivityCreate() {

    }
}
