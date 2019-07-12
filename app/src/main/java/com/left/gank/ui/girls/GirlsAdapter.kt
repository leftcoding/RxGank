package com.left.gank.ui.girls

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.left.gank.base.LazyFragment

/**
 * Create by LingYan on 2016-07-01
 */
class GirlsAdapter(fm: FragmentManager, private val mFragments: List<LazyFragment>, private val titles: List<String>) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}
