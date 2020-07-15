package com.left.gank.domain;


import android.business.domain.Solid;

import com.left.gank.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Create by LingYan on 2016-04-06
 */
public class GankResult extends BaseResult implements Serializable {

    private List<Solid> results;

    public GankResult(List<Solid> results) {
        this.results = results;
    }

    public List<Solid> getResults() {
        return results;
    }

    public boolean isEmpty() {
        return ListUtils.isEmpty(results);
    }

    public int getSize() {
        return ListUtils.getSize(results);
    }
}
