package com.left.gank.ui.discovered.teamBlog;

import com.left.gank.domain.JianDanBean;
import com.left.gank.mvp.ILoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-11-23
 */

public interface TeamBlogContract {
    interface View extends SupportView {
        void refillData(List<JianDanBean> list);

        void appendData(List<JianDanBean> list);
    }

    interface Presenter extends ILoadMorePresenter {

    }
}
