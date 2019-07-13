package com.left.gank.ui.more


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.left.gank.R
import com.left.gank.base.activity.BaseActivity
import com.left.gank.base.fragment.SupportFragment
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * 关于
 * Create by LingYan on 2016-05-10
 */
class AboutFragment : SupportFragment() {
    override fun fragmentLayoutId(): Int = R.layout.fragment_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        about_collapsing.apply {
            title = context!!.getString(R.string.navigation_about)
        }

        (activity as BaseActivity).apply {
            setSupportActionBar(about_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        about_toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }

        gank.setOnClickListener {
            gotoUrl(context?.getString(GANK_URL))
        }

        mzitu.setOnClickListener {
            gotoUrl(context?.getString(MEI_ZI_TU_URL))
        }

        github.setOnClickListener {
            gotoUrl(context?.getString(GIT_HUB_URL))
        }
    }

    private fun gotoUrl(url: String?) {
        url?.apply {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    companion object {
        private const val GANK_URL = R.string.about_gank_url
        private const val MEI_ZI_TU_URL = R.string.about_mzitu_url
        private const val GIT_HUB_URL = R.string.about_my_github_url
    }
}
