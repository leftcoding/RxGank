package com.coding.download;

/**
 * Create by LingYan on 2019-05-15
 */
public interface DownloadListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess();

    /**
     * @param progress 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载失败
     */
    void onDownloadFailed();
}
