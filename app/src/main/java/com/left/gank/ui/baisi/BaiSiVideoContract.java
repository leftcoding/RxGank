package com.left.gank.ui.baisi;

import com.left.gank.domain.BuDeJieVideo;
import com.left.gank.mvp.ILoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-30
 */

public interface BaiSiVideoContract {
    interface View extends SupportView {
        void refillData(List<BuDeJieVideo.ListBean> list);

        void appendData(List<BuDeJieVideo.ListBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
