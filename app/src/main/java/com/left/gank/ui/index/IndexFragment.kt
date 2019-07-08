package com.left.gank.ui.index

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.left.gank.R
import com.left.gank.config.Constants
import com.left.gank.ui.android.AndroidFragment
import com.left.gank.ui.base.fragment.SupportFragment
import com.left.gank.ui.ios.IosFragment
import com.left.gank.ui.welfare.WelfareFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Create by LingYan on 2016-04-22
 */
class IndexFragment : SupportFragment() {
    private val titles: List<String> = listOf(
            Constants.ANDROID,
            Constants.IOS,
            Constants.WELFRAE
    )

    private val fragments = listOf(
            AndroidFragment.newInstance(),
            IosFragment.newInstance(),
            WelfareFragment.newInstance()
    )

    override fun fragmentLayoutId(): Int = R.layout.fragment_main

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            activity!!.title = titles[position]
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = IndexPagerAdapter(childFragmentManager, fragments, titles)

        main_view_pager!!.apply {
            adapter = pagerAdapter
            offscreenPageLimit = fragments.size
            addOnPageChangeListener(onPageChangeListener)
        }

        tab_layout!!.apply {
            for (i in titles.indices) {
                addTab(newTab().setText(titles[i]))
            }
        }.also {
            it.setupWithViewPager(main_view_pager)
            it.tabMode = TabLayout.MODE_FIXED
            it.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        }
    }
}
