package com.start.permission;

import java.util.List;

public interface RequestCallback {
    void onGranted(List<String> list);

    void onDenied(List<String> list);
}
