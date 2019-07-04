package com.left.gank.network.service;

import com.left.gank.domain.CheckVersion;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * Create by LingYan on 2016-05-30
 */
public interface DownloadService {
    @Streaming
//    @GET("gankly.apk")
    @GET("")
    Observable<ResponseBody> downloadApk();

    @GET("checkVersion.json")
    Observable<CheckVersion> checkVersion();
}

