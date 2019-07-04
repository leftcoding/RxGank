package com.left.gank.ui.discovered.jiandan;

import android.content.Context;

import androidx.annotation.NonNull;

import com.left.gank.domain.JianDanBean;
import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-21
 */

public interface JiandanContract {
    interface View extends SupportView {
        void refillData(List<JianDanBean> list);

        void appendMoreDate(List<JianDanBean> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
