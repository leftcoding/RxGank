package com.left.gank.ui.discovered

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.left.gank.R
import com.left.gank.ui.base.fragment.ButterKnifeFragment
import com.left.gank.ui.discovered.jiandan.JiandanFragment
import com.left.gank.ui.discovered.more.DiscoveredAdapter
import com.left.gank.ui.discovered.more.DiscoveredMoreFragment
import com.left.gank.ui.discovered.teamBlog.TeamBlogFragment
import com.left.gank.ui.discovered.technology.TechnologyFragment
import com.left.gank.ui.discovered.video.VideoFragment
import kotlinx.android.synthetic.main.fragment_discovered.*

/**
 * 发现
 * Create by LingYan on 2016-07-01
 */
class DiscoveredFragment : ButterKnifeFragment() {
    private val titles: List<String> = listOf(
            TYPE_VIDEO,
            TYPE_JIAN_DAN,
            TYPE_THCHNOLOGY,
            TYPE_TEAM_BLOG,
            TYPE_MORE
    )

    private val fragments = listOf(
            VideoFragment(),
            JiandanFragment(),
            TechnologyFragment(),
            TeamBlogFragment(),
            DiscoveredMoreFragment()
    )

    override fun fragmentLayoutId(): Int = R.layout.fragment_discovered

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        discovered_view_pager.apply {
            val mPagerAdapter = DiscoveredAdapter(childFragmentManager, fragments, titles)
            adapter = mPagerAdapter
            offscreenPageLimit = mPagerAdapter.count
            addOnPageChangeListener(pageChangeListener)
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        discovered_tabLayout.apply {
            for (i in titles.indices) {
                addTab(newTab().setText(titles[i]))
            }
        }.also {
            it.setupWithViewPager(discovered_view_pager)
            it.tabMode = TabLayout.MODE_FIXED
            it.setSelectedTabIndicatorColor(ContextCompat.getColor(context!!, R.color.white))
        }
    }

    companion object {
        private const val TYPE_VIDEO = "视频"
        private const val TYPE_JIAN_DAN = "新鲜事"
        private const val TYPE_THCHNOLOGY = "科技资讯"
        private const val TYPE_TEAM_BLOG = "团队博客"
        private const val TYPE_MORE = "更多"
    }
}