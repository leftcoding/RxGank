package com.coding.file;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class FilePathUtils {
    private static final String DEFAULT_SPACE = "edu";

    public static String path = DEFAULT_SPACE;

    public FilePathUtils() {

    }

    public static void init(String space) {
        path = TextUtils.isEmpty(space) ? DEFAULT_SPACE : space;
        mkRootPath();
        mkHttpPath();
        mkImagePath();
        mkCrashPath();
        mkGalleryPath();
        mkApkPath();
    }

    private static void mkRootPath() {
        mkFile(path);
    }

    public static File mkHttpPath() {
        return mkChildFile(FilePath.PATH_HTTP);
    }

    public static File mkImagePath() {
        return mkChildFile(FilePath.PATH_IMAGE);
    }

    public static File mkCrashPath() {
        return mkChildFile(FilePath.PATH_CRASH);
    }

    public static File mkGalleryPath() {
        return mkChildFile(FilePath.PATH_GALLERY);
    }

    public static File mkApkPath() {
        return mkChildFile(FilePath.PATH_APK);
    }

    private static File mkChildFile(String name) {
        return mkFile(path + File.separator + name);
    }

    private static File mkFile(String name) {
        File appDir = new File(Environment.getExternalStorageDirectory(), name);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }
}
