package com.left.gank.ui.gallery

import android.business.domain.Gift

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class GalleryPagerAdapter internal constructor(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private var gifts: List<Gift>? = null
    private var url: String? = null
    internal fun setGifts(gifts: List<Gift>, url: String?) {
        this.gifts = gifts
        this.url = url
    }

    override fun getCount(): Int {
        return if (gifts == null) 0 else gifts!!.size
    }

    override fun getItem(position: Int): Fragment {
        return GalleryFragment.newInstance(gifts!![position])
    }
}