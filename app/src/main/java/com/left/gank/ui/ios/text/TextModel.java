package com.left.gank.ui.ios.text;

import android.ly.business.domain.Gank;

import com.left.gank.butterknife.item.ItemModel;
import com.left.gank.utils.DateUtils;

import java.util.Date;

/**
 * Create by LingYan on 2018-11-12
 */
public class TextModel extends ItemModel {
    Gank gank;

    public TextModel(Gank gank) {
        this.gank = gank;
    }

    public String getTime() {
        Date date = DateUtils.formatToDate(gank.publishedAt);
        return DateUtils.formatString(date, DateUtils.MM_DD);
    }

    @Override
    public int getViewType() {
        return ViewType.NORMAL;
    }
}
