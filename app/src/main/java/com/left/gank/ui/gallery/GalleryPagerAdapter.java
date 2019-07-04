package com.left.gank.ui.gallery;

import android.business.domain.Gift;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Gift> gifts;

    GalleryPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    @Override
    public int getCount() {
        return gifts == null ? 0 : gifts.size();
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.newInstance(gifts.get(position).imgUrl);
    }
}