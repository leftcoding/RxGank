package android.permission;

import android.permission.launcher.Launcher;

public interface Option {
    Request runtime();

    Launcher launcher();
}
