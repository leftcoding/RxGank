package com.left.gank.ui.discovered.video;

import android.business.domain.Gank;
import android.content.Context;

import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

import androidx.annotation.NonNull;


/**
 * Create by LingYan on 2017-01-03
 */

public interface VideoContract {
    interface View extends SupportView {
        void refillData(List<Gank> list);

        void appendData(List<Gank> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}

