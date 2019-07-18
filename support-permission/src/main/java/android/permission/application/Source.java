package android.permission.application;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public class Source {
    public Activity activity;

    public Fragment fragment;

    public Source(Activity activity) {
        this.activity = activity;
    }

    public Source(Fragment fragment) {
        this.fragment = fragment;
    }
}
