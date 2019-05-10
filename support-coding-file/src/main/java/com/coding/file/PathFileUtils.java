package com.coding.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class PathFileUtils {
    private PathFileUtils() {

    }

    private static void init(Context context) {
        context = context.getApplicationContext() == null ? context : context.getApplicationContext();
        String packageName = context.getPackageName();
        File appDir = new File(Environment.getExternalStorageDirectory(), "GankLy/network");
    }
}
