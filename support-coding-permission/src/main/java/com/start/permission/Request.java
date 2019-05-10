package com.start.permission;

public interface Request {
    Request checkPermission(String... permission);

    Request setCallback(RequestCallback callback);

    Request rationale(Runnable runnable);

    void start();
}
