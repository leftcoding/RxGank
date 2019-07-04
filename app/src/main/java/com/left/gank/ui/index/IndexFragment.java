package com.left.gank.ui.index;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.left.gank.R;
import com.left.gank.config.Constants;
import com.left.gank.domain.CheckVersion;
import com.left.gank.network.DownloadProgressListener;
import com.left.gank.ui.android.AndroidFragment;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.base.fragment.SupportFragment;
import com.left.gank.ui.ios.IosFragment;
import com.left.gank.ui.welfare.WelfareFragment;
import com.left.gank.view.ILauncher;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-04-22
 */
public class IndexFragment extends SupportFragment implements DownloadProgressListener, ILauncher {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    private List<String> titles;
    private LauncherPresenter presenter;
    private ProgressDialog progressDialog;
    private long appLength;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LazyFragment> fragments = new ArrayList<>();
        fragments.add(AndroidFragment.Companion.newInstance());
        fragments.add(IosFragment.newInstance());
        fragments.add(WelfareFragment.newInstance());

        titles = new ArrayList<>();
        titles.add(Constants.ANDROID);
        titles.add(Constants.IOS);
        titles.add(Constants.WELFRAE);

        IndexPagerAdapter mPagerAdapter = new IndexPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.addOnPageChangeListener(onPageChangeListener);

        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        presenter = new LauncherPresenter(getActivity(), this, this);
    }

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            getActivity().setTitle(titles.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        KLog.d("bytesRead:" + bytesRead + ",contentLength:" + contentLength + ",done:" + done);
        if (bytesRead > 0 && progressDialog != null && appLength > 0) {
            progressDialog.setProgress((int) bytesRead);
        }

        if (done) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void callUpdate(CheckVersion checkVersion) {
        appLength = checkVersion.getAppLength();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(checkVersion.getChangelog());
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton("更新", (dialog, which) -> {
            presenter.downloadApk();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage("更新中...");
            progressDialog.setMax((int) appLength);
            progressDialog.show();
        });
        builder.show();
    }

    @Override
    public void showDialog() {
    }

    @Override
    public void noNewVersion() {

    }

    @Override
    public void hiddenDialog() {
    }
}
