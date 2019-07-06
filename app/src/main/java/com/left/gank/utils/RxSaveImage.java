package com.left.gank.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.left.gank.BuildConfig;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import androidx.core.content.FileProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-04-20
 */
public class RxSaveImage {
    public static Observable<Bitmap> convertBitmap(final Context context, final String url) {
        return Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
            Bitmap bitmap = null;
            try {
                GlideUrl glideUrl;
                if (url.contains("meizitu") || url.contains("mzitu")) {
                    glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                            .addHeader("Referer", "http://www.mzitu.com/mm/")
                            .build());
                } else {
                    glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                            .build());
                }
                bitmap = Glide.with(context)
                        .asBitmap()
                        .load(glideUrl)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(true)
                        )
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                KLog.e(e);
                subscriber.onError(e);
            }
            if (bitmap != null) {
                subscriber.onNext(bitmap);
            } else {
                subscriber.onError(new Exception("bitmap can't be null"));
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 保存图片
     *
     * @param context  上下文
     * @param bitmap   图片
     * @param saveFile 图片存储地址
     * @return 返回存储图片Uri地址
     */
    public static Uri bitmapToImage(Context context, Bitmap bitmap, File saveFile) {
        Uri uri;
        try {
            FileOutputStream out = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            KLog.e(e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", saveFile);
        } else {
            uri = Uri.fromFile(saveFile);
        }
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
        return uri;
    }

    /**
     * 创建图片文件
     *
     * @param imageName 存储图片名称
     * @return 返回存储文件图片地址
     */
    public static File createImageFile(String imageName) {
        File imageFile = null;
        final String storagePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + File.separator + "pic";
        File appDir = new File(storagePath);
        boolean isFileExists = true;
        if (!appDir.exists()) {
            isFileExists = appDir.mkdirs();
        }
        if (isFileExists) {
            try {
                imageFile = File.createTempFile(imageName, ".jpg", appDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageFile;
    }
}
