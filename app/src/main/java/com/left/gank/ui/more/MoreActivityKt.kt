package com.left.gank.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.left.gank.R
import com.left.gank.ui.base.activity.BaseActivity
import com.left.gank.ui.collect.CollectFragment
import com.left.gank.ui.history.BrowseHistoryFragment

/**
 *
 * Create by LingYan on 2019-06-27
 */
class MoreActivityKt : BaseActivity() {
    companion object {
        private const val CONTENT_ID = R.id.setting_frame_layout
        private const val TYPE = "fromType"

        private const val TYPE_SETTING = 1
        private const val TYPE_COLLECT = 2
        private const val TYPE_BROWSE = 3
    }

    private var type: Int = 0

    override fun getContentId(): Int {
        return R.layout.activity_setting//To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        addFragment(getFragment(type))
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

    private fun parseIntent() {
        val bundle = intent.extras
        if (bundle != null) {
            type = bundle.getInt(TYPE)
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(CONTENT_ID, fragment)
                .commitAllowingStateLoss()
    }
}