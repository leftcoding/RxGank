package com.left.gank.ui.web.normal;

import android.lectcoding.ui.base.BaseView;
import androidx.annotation.NonNull;

import com.left.gank.data.entity.ReadHistory;
import com.left.gank.data.entity.UrlCollect;

/**
 * Create by LingYan on 2016-10-27
 */

public interface WebContract {
    interface View extends BaseView {
        void onCollect();

        void onCancelCollect();

        UrlCollect getCollect();

        void setCollectIcon(boolean isCollect);
    }

    interface Presenter {
        void findCollectUrl(@NonNull String url);

        void insetHistoryUrl(@NonNull ReadHistory readHistory);

        void cancelCollect();

        void collectAction(boolean isCollect);

        void collect();
    }
}
