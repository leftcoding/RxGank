package com.left.gank.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.permission.RequestCallback;
import android.permission.RequestExecutor;
import android.permission.Runnable;
import android.permission.RxPermission;

import com.left.gank.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Permission {
    public static void permissionApp(final Context context, final RequestCallback requestCallback, final String... permissions) {
        RxPermission.with(context)
                .runtime()
                .checkPermission(permissions)
                .rationale(new Runnable() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        showRationaleDialog(context, executor);
                    }
                })
                .setCallback(new RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                        if (requestCallback != null) {
                            requestCallback.onGranted(list);
                        }
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (RxPermission.hasAlwaysDeniedPermission(context, list)) {
                            showDeniedDialog(context, requestCallback);
                            return;
                        }
                        permissionApp(context, requestCallback, permissions);
                    }
                })
                .start();
    }

    public static void permissionApp(final Context context, final RequestCallback requestCallback, final String[]... permissions) {
        List<String> permission = new ArrayList<>();
        if (permissions != null && permissions.length > 0) {
            for (String[] strings : permissions) {
                permission.addAll(Arrays.asList(strings));
            }
        }
        permissionApp(context, requestCallback, permission.toArray(new String[0]));
    }

    private static void showRationaleDialog(final Context context, final RequestExecutor executor) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_title_tips)
                .setMessage(context.getString(R.string.permission_message_format, context.getString(R.string.app_name)))
                .setPositiveButton(R.string.permission_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        executor.execute();
                    }
                })
                .setNegativeButton(R.string.permission_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                })
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private static void showDeniedDialog(final Context context, final RequestCallback requestCallback) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_title_tips)
                .setMessage(context.getString(R.string.permission_message_format, context.getString(R.string.app_name)))
                .setPositiveButton(R.string.permission_setting, null)
                .setNegativeButton(R.string.permission_out, (dialog1, which) -> ((Activity) context).finish())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            dialog.dismiss();
            if (requestCallback != null) {
                requestCallback.onDenied(null);
            }
        });
    }

    /**
     * 启动权限设置页
     * 默认请求码是 RxPermission.RESULT_CODE： 100
     */
    public static void startPermissionSetting(Fragment fragment) {
        RxPermission.with(fragment).launcher().start(RxPermission.RESULT_CODE);
    }

    /**
     * 启动权限设置页
     * 默认请求码是 RxPermission.RESULT_CODE： 100
     */
    public static void startPermissionSetting(Activity activity) {
        RxPermission.with(activity).launcher().start(RxPermission.RESULT_CODE);
    }

    /**
     * 用在Fragment 页面请求权限
     */
    public static void checkPermission(final Fragment fragment, final RequestCallback requestCallback, final String... permissions) {
        final Context context = fragment.getContext();
        RxPermission.with(fragment)
                .runtime()
                .checkPermission(permissions)
                .rationale(new Runnable() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        checkDeniedDialog(context, requestCallback, executor);
                    }
                })
                .setCallback(new RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                        if (requestCallback != null) {
                            requestCallback.onGranted(list);
                        }
                    }

                    @Override
                    public void onDenied(List<String> list) {
                        if (RxPermission.hasAlwaysDeniedPermission(context, list)) {
                            checkDeniedDialog(context, requestCallback, null);
                            return;
                        }
                        checkPermission(fragment, requestCallback, permissions);
                    }
                })
                .start();
    }

    private static void checkDeniedDialog(final Context context, final RequestCallback requestCallback, final RequestExecutor executor) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_title_tips)
                .setMessage(context.getString(R.string.permission_message_format, context.getString(R.string.app_name)))
                .setPositiveButton(R.string.permission_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (executor != null) {
                            executor.execute();
                            return;
                        }
                        if (requestCallback != null) {
                            requestCallback.onDenied(null);
                        }
                    }
                })
                .setNegativeButton(R.string.permission_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
