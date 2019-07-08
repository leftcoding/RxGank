package com.left.gank.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.left.gank.R
import com.left.gank.ui.base.activity.BaseActivity
import com.left.gank.ui.collect.CollectFragment
import com.left.gank.ui.history.BrowseHistoryFragment

/**
 * Create by LingYan on 2016-09-21
 */

class MoreActivity : BaseActivity() {
    private var type: Int = 0

    override fun getContentId(): Int = R.layout.activity_setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        getFragment(type).apply {
            addFragment(this)
        }
    }

    private fun parseIntent() {
        intent.extras?.apply {
            type = getInt(TYPE)
        }
    }

    private fun getFragment(type: Int): Fragment {
        val fragment: Fragment
        when (type) {
            TYPE_COLLECT -> fragment = CollectFragment()
            TYPE_BROWSE -> fragment = BrowseHistoryFragment()
            TYPE_SETTING -> fragment = SettingFragment()
            else -> fragment = SettingFragment()
        }
        return fragment
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.setting_frame_layout, fragment)
                .commitAllowingStateLoss()
    }

    companion object {
        const val TITLE = "title"
        const val TYPE = "from_type"

        const val TYPE_SETTING = 1
        const val TYPE_COLLECT = 2
        const val TYPE_BROWSE = 3
    }
}
