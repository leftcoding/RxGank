package com.left.gank.ui.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.left.gank.BuildConfig;
import com.left.gank.bean.CheckVersion;
import com.left.gank.mvp.base.BasePresenter;
import com.left.gank.network.DownloadProgressListener;
import com.left.gank.network.api.DownloadApi;
import com.left.gank.utils.CrashUtils;
import com.left.gank.utils.FileUtils;
import com.left.gank.view.ILauncher;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2016-06-01
 */
public class LauncherPresenter extends BasePresenter<ILauncher> {
    private DownloadApi mDownloadApi;
    private String apkName = "gankly.apk";
    private File mFile = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/GankLy/" + apkName);
    private Context mContext;

    public LauncherPresenter(Activity mActivity, ILauncher view) {
        this(mActivity, view, null);
    }

    public LauncherPresenter(Activity activity, ILauncher view, DownloadProgressListener listener) {
        super(activity.getApplicationContext(), view);
        mContext = activity;
        mDownloadApi = new DownloadApi(listener);
    }

    public void checkVersion() {
        view.showDialog();
        mDownloadApi.checkVersion(new Observer<CheckVersion>() {
            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
                view.hiddenDialog();
            }

            @Override
            public void onComplete() {
                view.hiddenDialog();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CheckVersion checkVersion) {
                int curVersion = BuildConfig.VERSION_CODE;
                if (checkVersion.getCode() > curVersion) {
                    view.callUpdate(checkVersion);
                }
            }
        });
    }

    public void downloadApk() {
        mDownloadApi.downloadApk(inputStream -> {
            try {
                FileUtils.writeFile(inputStream, mFile);
            } catch (IOException e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }
        }, new Observer<InputStream>() {

            @Override
            public void onError(Throwable e) {
                KLog.e(e);
                CrashUtils.crashReport(e);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(InputStream inputStream) {
                downloadSuccess(mContext, mFile);
            }
        });
    }

    private void downloadSuccess(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {

    }
}
