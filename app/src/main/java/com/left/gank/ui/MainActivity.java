package com.left.gank.ui;

import android.os.Bundle;

import com.left.gank.R;
import com.left.gank.rxjava.RxBus_;
import com.left.gank.ui.base.activity.BaseActivity;
import com.left.gank.ui.discovered.DiscoveredFragment;
import com.left.gank.ui.girls.GirlsFragment;
import com.left.gank.ui.index.IndexFragment;
import com.left.gank.ui.mine.MineFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-6-13
 */
public class MainActivity extends BaseActivity {
    private static final int DEFAULT_TAB_POSITION = 0;

    private static final int TAB_HOME = 0;
    private static final int TAB_NEWS = 1;
    private static final int TAB_IMAGE = 2;
    private static final int TAB_MORE = 3;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private Fragment curFragment;
    private List<Fragment> fragmentList;

    @Override
    protected int getContentId() {
        return R.layout.fragment_main_bottom_navigation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = getFragmentList();
        bottomBar.setDefaultTabPosition(DEFAULT_TAB_POSITION);
        bottomBar.setOnTabSelectListener(onTabSelectListener);
    }

    private final OnTabSelectListener onTabSelectListener = tabId -> {
        int index = getFragmentIndex(tabId);
        openFragment(index);
    };

    private int getFragmentIndex(int tabId) {
        int index = TAB_HOME;
        switch (tabId) {
            case R.id.tab_home:
                index = TAB_HOME;
                break;
            case R.id.tab_news:
                index = TAB_NEWS;
                break;
            case R.id.tab_image:
                index = TAB_IMAGE;
                break;
            case R.id.tab_more:
                index = TAB_MORE;
                break;
        }
        return index;
    }

    private void openFragment(int index) {
        Fragment fragmentTo = fragmentList.get(index);
        if (curFragment == null) {
            addMainFragment(fragmentTo);
            curFragment = fragmentTo;
        } else {
            if (!curFragment.equals(fragmentTo)) {
                addAnimFragment(curFragment, fragmentTo, true);
                curFragment = fragmentTo;
            }
        }
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new IndexFragment());
        fragments.add(new DiscoveredFragment());
        fragments.add(new GirlsFragment());
        fragments.add(new MineFragment());
        return fragments;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus_.getInstance().removeAllStickyEvents();// 移除所有Sticky事件
    }
}
