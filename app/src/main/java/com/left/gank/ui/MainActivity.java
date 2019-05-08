package com.left.gank.ui;

import android.os.Bundle;
import android.view.KeyEvent;

import com.left.gank.R;
import com.left.gank.rxjava.RxBus_;
import com.left.gank.ui.base.activity.BaseActivity;
import com.left.gank.ui.discovered.DiscoveredFragment;
import com.left.gank.ui.girls.GirlsFragment;
import com.left.gank.ui.main.IndexFragment;
import com.left.gank.ui.mine.MineFragment;
import com.left.gank.utils.permission.PermissionUtils;
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

    private long keyDownTime;
    private Fragment curFragment;

    private List<Fragment> fragmentList;
    private int index = 0;
    private boolean isRestore = false;

    @Override
    protected int getContentId() {
        return R.layout.fragment_main_bottom_navigation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isRestore = savedInstanceState.getBoolean("isRestore");
            index = savedInstanceState.getInt("index");
        }

        PermissionUtils.requestAllPermissions(this);

        fragmentList = getFragmentList();
        bottomBar.setDefaultTabPosition(DEFAULT_TAB_POSITION);

        bottomBar.setOnTabSelectListener(onTabSelectListener);
    }

    private final OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(int tabId) {
            index = getFragmentIndex(tabId);
            openFragment(index);
        }
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
            if (!isRestore) {
                addMainFragment(fragmentTo);
            }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mIndex != 0) {
//                mBottomBar.selectTabAtPosition(0);
//                return false;
//            } else if ((System.currentTimeMillis() - mKeyDownTime) > 2000) {
//                mKeyDownTime = System.currentTimeMillis();
//                ToastUtils.shortBottom(getBaseContext(), R.string.app_again_out);
//                return false;
//            } else {
//                finish();
//                AppUtils.killProcess();
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus_.getInstance().removeAllStickyEvents();// 移除所有Sticky事件
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isRestore", true);
        outState.putInt("index", index);
        super.onSaveInstanceState(outState);
    }
}
