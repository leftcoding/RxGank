package com.left.gank.ui.index;

import com.left.gank.ui.base.LazyFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class IndexPagerAdapter extends FragmentStatePagerAdapter {
    private List<LazyFragment> fragments;
    private List<String> titles;

    public IndexPagerAdapter(FragmentManager fm, List<LazyFragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}