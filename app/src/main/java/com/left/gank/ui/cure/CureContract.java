package com.left.gank.ui.cure;

import android.business.domain.Gift;
import android.content.Context;

import com.left.gank.mvp.base.LoadMorePresenter;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Create by LingYan on 2016-10-26
 */

public interface CureContract {
    interface View extends com.left.gank.mvp.base.SupportView {
        void loadDataSuccess(int page, int maxPage, List<Gift> list);

        void loadDataFailure(int page, String msg);

        void showLoadingDialog();

        void hideLoadingDialog();

        void loadImagesSuccess(List<Gift> list);

        void loadImagesFailure(String msg);
    }

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void loadData(int page);

        abstract void loadImages(String url);
    }
}
