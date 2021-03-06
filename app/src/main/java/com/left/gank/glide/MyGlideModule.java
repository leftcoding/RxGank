package com.left.gank.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.Excludes;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.left.gank.utils.FileUtils;

import java.io.InputStream;

import androidx.annotation.NonNull;

@Excludes(OkHttpProgressGlideModule.class)
@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        String diskCache = FileUtils.getGlideDefaultPath(context);
        builder.setDiskCache(new DiskLruCacheFactory(diskCache, 100 * 1024 * 1024));
        builder.setDefaultRequestOptions(new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
        );
        builder.setLogLevel(Log.DEBUG);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(context));
    }
}