package com.start.permission;

import com.start.permission.launcher.Launcher;

public interface Option {
    Request runtime();

    Launcher launcher();
}
