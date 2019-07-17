package com.left.gank.ui

import android.os.Bundle
import android.rxbus.RxApiManager
import androidx.fragment.app.Fragment
import com.left.gank.R
import com.left.gank.base.activity.BaseActivity
import com.left.gank.ui.discovered.DiscoveredFragment
import com.left.gank.ui.girls.GirlsFragment
import com.left.gank.ui.index.IndexFragment
import com.left.gank.ui.mine.MineFragment
import kotlinx.android.synthetic.main.fragment_main_bottom_navigation.*

/**
 *
 * Create by LingYan on 2019-06-29
 */
class MainActivity : BaseActivity() {
    private var curFragment: Fragment? = null

    private val fragmentList = listOf(
            IndexFragment(),
            DiscoveredFragment(),
            GirlsFragment(),
            MineFragment()
    )

    companion object {
        const val TAB_HOME = 0
        const val TAB_NEWS = 1
        const val TAB_IMAGE = 2
        const val TAB_MORE = 3
    }

    override fun getContentId(): Int = R.layout.fragment_main_bottom_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottom_navigation!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> {
                    openFragment(TAB_HOME)
                    true
                }
                R.id.tab_news -> {
                    openFragment(TAB_NEWS)
                    true
                }
                R.id.tab_image -> {
                    openFragment(TAB_IMAGE)
                    true
                }
                R.id.tab_more -> {
                    openFragment(TAB_MORE)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.selectedItemId = R.id.tab_home
    }

    private fun openFragment(index: Int) {
        val toFragment = fragmentList[index]
        if (curFragment == null) {
            addMainFragment(toFragment)
            curFragment = toFragment
        } else {
            if (curFragment != toFragment) {
                addAnimFragment(curFragment, toFragment, true)
                curFragment = toFragment
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxApiManager.get().clear()
    }
}