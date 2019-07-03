package com.left.gank.ui.pure;

import android.business.domain.Gift;
import android.content.Context;

import androidx.annotation.NonNull;

import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-12-27
 */

public interface PureContract {
    interface View extends SupportView {
        void loadDataSuccess(int page, List<Gift> list);

        void loadDataFailure(int page, String msg);

        void showLoadingDialog();

        void hideLoadingDialog();

        void loadImagesSuccess(List<Gift> list);

        void loadImagesFailure(String msg);
    }

    abstract class Presenter extends LoadMorePresenter<View> {

        public Presenter(@NonNull Context context, @NonNull View view) {
            super(context, view);
        }

        abstract void loadData(int page);

        abstract void loadImages(String url);
    }
}
