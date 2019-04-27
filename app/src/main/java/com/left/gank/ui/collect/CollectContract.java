package com.left.gank.ui.collect;

import com.left.gank.data.entity.UrlCollect;
import com.left.gank.mvp.ILoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-20
 */

public interface CollectContract {

    interface View extends SupportView {
        void setAdapterList(List<UrlCollect> list);

        void appendAdapter(List<UrlCollect> list);

        void onDelete();

        int getItemsCount();

        void revokeCollect();
    }

    abstract class Presenter implements ILoadMorePresenter {
        abstract void cancelCollect(long position);

        abstract void insertCollect(UrlCollect collect);
    }
}
