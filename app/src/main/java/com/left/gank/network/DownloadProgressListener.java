package com.left.gank.network;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}