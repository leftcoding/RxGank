package com.left.gank.ui.mine;

import android.content.Intent;
import android.download.Download;
import android.download.DownloadListener;
import android.file.FilePathUtils;
import android.os.Bundle;
import android.ui.logcat.Logcat;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.left.gank.R;
import com.left.gank.ui.base.fragment.ButterKnifeFragment;
import com.left.gank.ui.more.MoreActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 我的
 * Create by LingYan on 2016-09-21
 */

public class MineFragment extends ButterKnifeFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mine_nested_scroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Download download;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(R.string.navigation_mine);
        nestedScrollView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        download = new Download.Builder()
                .savePath(FilePathUtils.mkApkPath())
                .url("http://b8.market.mi-img.com/download/AppStore/045ac05c5b03b42730855cfeef96cecc4f61d1143/com.duia.duiaapp.apk")
                .downloadListener(downloadListener)
                .build();
    }

    @OnClick(R.id.mine_rl_setting)
    void onSetting() {
        openActivity(MoreActivity.TYPE_SETTING);
    }

    @OnClick(R.id.mine_rl_collect)
    void onCollect() {
        openActivity(MoreActivity.TYPE_COLLECT);
    }

    @OnClick(R.id.mine_rl_browse)
    void onBrowse() {
        openActivity(MoreActivity.TYPE_BROWSE);
    }

    @OnClick(R.id.download)
    void download() {
        if (download != null) {
            if (download.isDownloading()) {
                download.cancel();
            }
            download.download();
        }
    }

    private final DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onSuccess() {
            Logcat.d(">>onDownloadSuccess");
        }

        @Override
        public void onProgress(int progress) {
            if (progressBar != null) {
                progressBar.setProgress(progress);
            }
        }

        @Override
        public void onFailure(String msg) {
            Logcat.d(">>onDownloadFailed");
        }
    };

    private void openActivity(int type) {
        Intent intent = new Intent(getContext(), MoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MoreActivity.TYPE, type);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (download != null) {
            download.cancel();
        }
    }
}
