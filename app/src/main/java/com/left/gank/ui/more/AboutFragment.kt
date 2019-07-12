package com.left.gank.ui.more


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.left.gank.R
import com.left.gank.base.activity.BaseActivity
import com.left.gank.base.fragment.SupportFragment
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * 关于
 * Create by LingYan on 2016-05-10
 * Email:137387869@qq.com
 */
class AboutFragment : SupportFragment() {
    private val GANK_URL = R.string.about_gank_url
    private val MEI_ZI_TU_URL = R.string.about_mzitu_url
    private val GIT_HUB_URL = R.string.about_my_github_url

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
    }


    @OnClick(R.id.gank)
    internal fun clickGank() {
        gotoUrl(context?.getString(GANK_URL))
    }

    @OnClick(R.id.mzitu)
    internal fun clickMzitu() {
        gotoUrl(context?.getString(MEI_ZI_TU_URL))
    }

    @OnClick(R.id.github)
    internal fun clickGithub() {
        gotoUrl(context?.getString(GIT_HUB_URL))
    }

    private fun gotoUrl(url: String?) {
        url?.apply {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
