package com.start.permission;

import com.start.permission.andpermission.RxAndPermission;
import com.start.permission.launcher.AllLauncher;
import com.start.permission.launcher.Launcher;
import com.start.permission.source.Source;

public class BootOption implements Option {
    private Source source;

    public BootOption(Source source) {
        this.source = source;
    }

    @Override
    public Request runtime() {
        return new RxAndPermission(source.getContext());
    }

    @Override
    public Launcher launcher() {
        return new AllLauncher(source);
    }
}
