package com.left.gank.ui.baisi.image;

import android.content.Context;
import androidx.annotation.NonNull;

import com.left.gank.bean.BuDeJieBean;
import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-05
 */

public interface BaiSiImageContract {
    interface View extends SupportView {
        void refillData(List<BuDeJieBean.ListBean> list);

        void appendData(List<BuDeJieBean.ListBean> list);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }
    }
}
