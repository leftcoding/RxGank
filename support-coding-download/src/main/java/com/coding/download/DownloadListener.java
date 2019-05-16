package com.coding.download;

/**
 * Create by LingYan on 2019-05-15
 */
public interface DownloadListener {
    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 下载进度
     */
    void onProgress(int progress);

    /**
     * 下载失败
     */
    void onFailure(String msg);
}
