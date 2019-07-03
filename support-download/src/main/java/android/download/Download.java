package android.download;

import android.download.interceptor.RangeInterceptor;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

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
    private OkHttpClient okHttpClient;
    private DownloadListener downloadListener;
    private Call call;

    private boolean isDownloading;
    private String url;
    private File saveFile;
    private String fileName;
    private long startPoint;

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
            if (responseBody == null) {
                if (downloadListener != null) {
                    downloadListener.onFailure(response.message());
                }
                return;
            }

            File saveFile = saveFile();
            if (saveFile != null) {
//                saveFileToDisk(responseBody, saveFile);
                saveFile(responseBody, saveFile);
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

    private void saveFile(ResponseBody responseBody, File saveFile) {
        FileChannel fileChannel = null;
        int total = (int) responseBody.contentLength();
        int currentLength = 0;
        InputStream inputStream = responseBody.byteStream();

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile, "rws");
            fileChannel = randomAccessFile.getChannel();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startPoint, total);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                currentLength = currentLength + len;
                mappedByteBuffer.put(buffer, 0, len);
                int progress = (int) (currentLength * 1.0f / total * 100);
                if (downloadListener != null) {
                    downloadListener.onProgress(progress);
                }
            }
            if (downloadListener != null) {
                downloadListener.onSuccess();
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFailure(e.toString());
        } finally {
            isDownloading = false;
            try {
                inputStream.close();
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        startPoint = lastSaveLength();
        Request request = new Request.Builder().url(url).build();
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(new RangeInterceptor(startPoint))
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
     * 如果有存在文件删除
     */
    private String saveAbsolutePath(File downloadFile) {
        downloadFile.deleteOnExit();
        downloadFile.mkdirs();
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
