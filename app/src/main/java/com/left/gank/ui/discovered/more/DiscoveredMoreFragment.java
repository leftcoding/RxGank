package com.left.gank.ui.discovered.more;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.left.gank.R;
import com.left.gank.ui.MainActivity;
import com.left.gank.ui.baisi.BaiSiActivity;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.utils.theme.ThemeColor;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by LingYan on 2016-12-02
 */

public class DiscoveredMoreFragment extends LazyFragment {
    @BindView(R.id.discovered_parent)
    LinearLayout mLinearLayout;

    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    protected void callBackRefreshUi() {
        ThemeColor color = new ThemeColor(this);
        color.setBackgroundResource(R.attr.themeBackground, mLinearLayout);
        color.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovered_more;
    }

    @OnClick(R.id.discovered_rl_budejie)
    void onClickBuDeJie() {
        mActivity.startActivity(new Intent(mActivity, BaiSiActivity.class));
        mActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void onLazyActivityCreate() {

    }
}
