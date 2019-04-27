package com.left.gank.ui.history;

import android.content.Context;
import androidx.annotation.NonNull;

import com.left.gank.data.entity.ReadHistory;
import com.left.gank.mvp.base.LoadMorePresenter;
import com.left.gank.mvp.base.SupportView;

import java.util.List;

/**
 * Create by LingYan on 2016-10-31
 */

public interface BrowseHistoryContract {
    interface View extends SupportView {
        void refillData(List<ReadHistory> history);

        void appendData(List<ReadHistory> history);
    }

    abstract class Presenter extends LoadMorePresenter<View> {
        public Presenter(@NonNull Context context, View view) {
            super(context, view);
        }

        abstract void deleteHistory(long id);
    }
}
