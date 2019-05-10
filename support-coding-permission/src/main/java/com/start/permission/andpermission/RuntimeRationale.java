package com.start.permission.andpermission;

import android.content.Context;

import com.start.permission.Runnable;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class RuntimeRationale implements Rationale<List<String>> {
    private final Runnable runnable;
    private RequestExecutor executor;

    public RuntimeRationale(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        this.executor = executor;
        if (runnable != null) {
            runnable.showRationale(context, permissionNames, requestExecutor);
        }
    }

    private final com.start.permission.RequestExecutor requestExecutor = new com.start.permission.RequestExecutor() {
        @Override
        public void execute() {
            if (executor != null) {
                executor.execute();
            }
        }

        @Override
        public void cancel() {
            if (executor != null) {
                executor.cancel();
            }
        }
    };
}
