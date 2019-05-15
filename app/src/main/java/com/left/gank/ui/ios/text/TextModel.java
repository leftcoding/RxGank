package com.left.gank.ui.ios.text;

import android.ly.business.domain.Gank;
import android.text.TextUtils;

import com.left.gank.butterknife.diff.ItemComparator;
import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.DateUtils;

import java.util.Date;

/**
 * Create by LingYan on 2018-11-12
 */
public class TextModel extends ItemModel {
    final Gank gank;

    public TextModel(Gank gank) {
        this.gank = gank;
    }

    public String getTime() {
        Date date = DateUtils.formatToDate(gank.publishedAt);
        return DateUtils.formatString(date, DateUtils.MM_DD);
    }

    @Override
    public boolean areItemsTheSame(ItemComparator itemComparator) {
        if (itemComparator instanceof TextModel) {
            TextModel old = (TextModel) itemComparator;
            return old.getViewType() == getViewType();
        }
        return super.areItemsTheSame(itemComparator);
    }

    @Override
    public boolean areContentsTheSame(ItemComparator itemComparator) {
        if (itemComparator instanceof TextModel) {
            TextModel old = (TextModel) itemComparator;
            Gank oldGank = old.gank;
            return TextUtils.equals(oldGank.id, gank.id);
        }
        return super.areContentsTheSame(itemComparator);
    }

    @Override
    public int getViewType() {
        return ViewType.NORMAL;
    }
}
