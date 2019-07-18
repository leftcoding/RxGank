package android.permission;

import android.permission.andpermission.RAndPermission;
import android.permission.launcher.AllLauncher;
import android.permission.launcher.Launcher;
import android.permission.source.Source;

public class BootOption implements Option {
    private Source source;

    public BootOption(Source source) {
        this.source = source;
    }

    @Override
    public Request runtime() {
        return new RAndPermission(source.getContext());
    }

    @Override
    public Launcher launcher() {
        return new AllLauncher(source);
    }
}
