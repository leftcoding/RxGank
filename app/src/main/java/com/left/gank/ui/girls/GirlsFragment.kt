package com.left.gank.ui.girls

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.left.gank.R
import com.left.gank.base.LazyFragment
import com.left.gank.base.fragment.SupportFragment
import com.left.gank.config.Constants
import com.left.gank.ui.cure.CureFragment
import com.left.gank.ui.daily.DailyGirlFragment
import com.left.gank.ui.pure.PureFragment
import kotlinx.android.synthetic.main.fragment_girls.*

/**
 * 美しい妹
 * Create by LingYan on 2016-07-01
 */
class GirlsFragment : SupportFragment() {
    private lateinit var girlsAdapter: GirlsAdapter
    private val titles = arrayListOf<String>()
    private val fragments = arrayListOf<LazyFragment>()

    init {
        titles.add(Constants.QINGCHUN)
        titles.add(Constants.CURE)
        titles.add(Constants.DAILY_GIRL)

        fragments.add(PureFragment())
        fragments.add(CureFragment())
        fragments.add(DailyGirlFragment())
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_girls
    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        girlsAdapter = GirlsAdapter(childFragmentManager, fragments, titles)
        girl_view_pager!!.apply {
            adapter = girlsAdapter
            offscreenPageLimit = fragments.size
            addOnPageChangeListener(pageChangeListener)
            currentItem = 0
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        girl_tabLayout!!.apply {
            for (i in titles.indices) {
                addTab(newTab().setText(titles[i]))
            }
            setupWithViewPager(girl_view_pager)
            tabMode = TabLayout.MODE_FIXED
            setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}