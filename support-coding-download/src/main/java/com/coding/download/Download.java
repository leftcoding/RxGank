package com.coding.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Create by LingYan on 2019-05-15
 */
public class Download {
    private final OkHttpClient okHttpClient;
    private DownloadListener downloadListener;
    private Call call;

    private boolean isDownloading;
    private String url;
    private File saveFile;
    private String fileName;

    private Download(DownloadParams downloadParams) {
        this.url = downloadParams.url;
        this.saveFile = downloadParams.saveFile;
        this.fileName = downloadParams.fileName;
        this.downloadListener = downloadParams.downloadListener;
        okHttpClient = new OkHttpClient();
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            isDownloading = false;
            if (downloadListener != null) {
                downloadListener.onFailure(e.toString());
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful() || responseBody == null) {
                if (downloadListener != null) {
                    downloadListener.onFailure(response.message());
                }
                return;
            }

            File saveFile = saveFile();
            if (saveFile != null) {
                saveFileToDisk(responseBody, saveFile);
            }
        }
    };

    private void onFailure(String msg) {
        if (downloadListener != null) {
            downloadListener.onFailure(msg);
        }
    }

    /**
     * 保存文件绝对地址
     */
    private File saveFile() {
        String savePath = saveAbsolutePath(saveFile);
        String fileName = getNameFromUrl(url);
        try {
            return new File(savePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e.toString());
        }
        return null;
    }

    /**
     * 之前下载文件大小
     */
    private long lastSaveLength() {
        File file = saveFile();
        return file == null ? 0 : file.length();
    }

    private void saveFileToDisk(ResponseBody responseBody, File saveFile) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[2048];
        int length;
        try {
            inputStream = responseBody.byteStream();
            final long total = responseBody.contentLength();
            fos = new FileOutputStream(saveFile);
            long sum = 0;
            while ((length = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, length);
                sum += length;
                int progress = (int) (sum * 1.0f / total * 100);
                if (downloadListener != null) {
                    downloadListener.onProgress(progress);
                }
            }
            fos.flush();
            if (downloadListener != null) {
                downloadListener.onSuccess();
            }
        } catch (Exception e) {
            onFailure(e.toString());
        } finally {
            isDownloading = false;
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void download() {
        if (isDownloading) {
            return;
        }
        isDownloading = true;
//        long lastFileLength = lastSaveLength();
        Request request = new Request.Builder()
                // TODO 断点下载，文件是否修改
//                .header("If-Modified-Since", "Fri, 10 May 2019 10:37:08 GMT")
//                .header("RANGE", "bytes=" + lastFileLength + "-80824136")
                .url(url)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    /**
     * 判断下载目录是否存在
     */
    private String saveAbsolutePath(File downloadFile) {
        if (!downloadFile.mkdirs()) {
            downloadFile.mkdirs();
        }
        return downloadFile.getAbsolutePath();
    }

    /**
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static final class Builder {
        private DownloadParams downloadParams;

        public Builder() {
            downloadParams = new DownloadParams();
        }

        public Builder url(String url) {
            downloadParams.url = url;
            return this;
        }

        public Builder fileName(String fileName) {
            downloadParams.fileName = fileName;
            return this;
        }

        public Builder savePath(File savePath) {
            downloadParams.saveFile = savePath;
            return this;
        }

        public Builder downloadListener(DownloadListener downloadListener) {
            downloadParams.downloadListener = downloadListener;
            return this;
        }

        public Download build() {
            return new Download(downloadParams);
        }
    }
}
