package com.left.gank.ui.welfare;

import android.content.Context;
import android.lectcoding.ui.base.BaseView;
import android.ly.business.domain.Gank;
import androidx.annotation.NonNull;

import com.left.gank.mvp.base.LoadMorePresenter;

import java.util.List;

/**
 * Create by LingYan on 2016-12-23
 */

public interface WelfareContract {
    interface View extends BaseView {
        void loadWelfareSuccess(int page, List<Gank> list);

        void loadWelfareFailure(String msg);
    }

    public abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        public abstract void loadWelfare(int page);
    }
}
