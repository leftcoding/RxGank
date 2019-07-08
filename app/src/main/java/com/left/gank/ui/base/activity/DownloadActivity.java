package com.left.gank.ui.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.left.gank.network.DownloadProgressListener;

public abstract class DownloadActivity extends SupportActivity implements DownloadProgressListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
    }
}
