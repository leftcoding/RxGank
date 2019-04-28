package com.left.gank.ui.cure;

import android.content.Context;
import android.ly.business.domain.Gift;
import androidx.annotation.NonNull;

import com.left.gank.mvp.base.LoadMorePresenter;

import java.util.ArrayList;

/**
 * Create by LingYan on 2016-10-26
 */

public interface CureContract {
    interface View extends com.left.gank.mvp.base.SupportView {
        void setMaxProgress(int value);

        void disProgressDialog();

        void openBrowseActivity(ArrayList<Gift> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void refreshGirls();

        abstract void appendGirls();

        abstract void loadImages(String url);
    }
}
