package com.left.gank.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.left.gank.R
import com.left.gank.base.fragment.SupportFragment
import com.left.gank.ui.more.MoreActivity
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * 我的
 * Create by LingYan on 2016-09-21
 */

class MineFragment : SupportFragment() {
    override fun fragmentLayoutId(): Int = R.layout.fragment_mine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setTitle(R.string.navigation_mine)
        mine_rl_setting!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_SETTING)
        }
        mine_rl_collect!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_COLLECT)
        }
        mine_rl_browse!!.setOnClickListener {
            startActivity(MoreActivity.TYPE_BROWSE)
        }
    }

    private fun startActivity(type: Int) {
        Intent(context, MoreActivity::class.java).apply {
            putExtra(MoreActivity.TYPE, type)
            startActivity(this)
        }
    }
}
