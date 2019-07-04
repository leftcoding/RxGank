package com.left.gank.ui.welfare;

import android.business.domain.Gank;
import android.content.Context;
import android.ui.base.BaseView;

import com.left.gank.mvp.base.LoadMorePresenter;

import java.util.List;

import androidx.annotation.NonNull;


/**
 * Create by LingYan on 2016-12-23
 */

public interface WelfareContract {
    interface View extends BaseView {
        void loadWelfareSuccess(int page, List<Gank> list);

        void loadWelfareFailure(int page, String msg);
    }

    public abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        public abstract void loadWelfare(boolean refresh, boolean useProgress, int page);
    }
}
