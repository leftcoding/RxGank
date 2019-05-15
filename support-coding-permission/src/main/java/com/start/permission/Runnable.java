package com.start.permission;

import android.content.Context;

import java.util.List;

public interface Runnable {
    void showRationale(Context context, List<String> permissions, final RequestExecutor executor);
}
