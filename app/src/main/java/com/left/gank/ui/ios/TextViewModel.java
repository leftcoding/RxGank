package com.left.gank.ui.ios;

import android.ly.business.domain.Gank;

import com.left.gank.butterknife.diff.ItemComparator;

import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.DateUtils;

import java.util.Date;

/**
 * Create by LingYan on 2018-11-12
 */
public class TextViewModel extends ItemModel {
    Gank gank;

    TextViewModel(Gank gank) {
        this.gank = gank;
    }

    public String getTime() {
        Date date = DateUtils.formatToDate(gank.publishedAt);
        return DateUtils.formatString(date, DateUtils.MM_DD);
    }

    @Override
    public boolean areItemsTheSame(ItemComparator itemComparator) {
        return itemComparator instanceof TextViewModel;
    }

    @Override
    public boolean areContentsTheSame(ItemComparator itemComparator) {
        if (itemComparator instanceof TextViewModel) {
            TextViewModel oldItem = (TextViewModel) itemComparator;
            return gank.isContentEqual(oldItem.gank);
        }
        return false;
    }

    @Override
    public int getViewType() {
        return ViewType.NORMAL;
    }
}
