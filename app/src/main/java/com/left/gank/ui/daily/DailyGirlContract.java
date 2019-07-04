package com.left.gank.ui.daily;

import android.business.domain.Gift;
import android.business.domain.Girl;
import android.content.Context;

import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


/**
 * Create by LingYan on 2016-10-26
 */

public interface DailyGirlContract {
    interface View extends SupportView {
        void refillData(List<Girl> list);

        void appendItem(List<Girl> list);

        void setMaxProgress(int value);

        void disProgressDialog();

        void openBrowseActivity(ArrayList<Gift> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void girlsImages(String url);
    }
}
