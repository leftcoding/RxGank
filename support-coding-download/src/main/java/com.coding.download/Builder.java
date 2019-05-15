package com.coding.download;

/**
 * Create by LingYan on 2019-05-15
 */
public class Builder {
    String filePath;

    String fileName;

    String url;

    public Builder() {
    }

    /**
     * 设置文件存储位置
     *
     * @param filePath 文件存储相对地址
     */
    public Builder filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * 设置文件存储名称
     *
     * @param fileName 文件存储名称
     */
    public Builder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * 设置请求文件网址
     *
     * @param url 请求文件网址
     */
    public Builder url(String url) {
        this.url = url;
        return this;
    }
}