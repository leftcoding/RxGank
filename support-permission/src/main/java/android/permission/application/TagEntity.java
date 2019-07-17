package android.permission.application;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public class TagEntity {
    public Activity activity;

    public Fragment fragment;

    public TagEntity(Activity activity) {
        this.activity = activity;
    }

    public TagEntity(Fragment fragment) {
        this.fragment = fragment;
    }
}
