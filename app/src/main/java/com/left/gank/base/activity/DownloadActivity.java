package com.left.gank.base.activity;

import android.os.Bundle;

import com.left.gank.network.DownloadProgressListener;

import androidx.annotation.Nullable;

public abstract class DownloadActivity extends SupportActivity implements DownloadProgressListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
    }
}
