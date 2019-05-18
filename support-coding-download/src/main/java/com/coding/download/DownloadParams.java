package com.coding.download;

import android.content.Context;

import java.io.File;

/**
 * Create by LingYan on 2019-05-15
 */
public class DownloadParams {
    /**
     * 下载文件保存地址
     */
    public File saveFile;

    /**
     * 下载文件命名
     */
    public String fileName;

    /**
     * 文件下载地址
     */
    public String url;

    /**
     * 下载监听
     */
    public DownloadListener downloadListener;

    /**
     * 是否断点续传
     */
    public boolean isBreakpoint;

    public Context context;
}