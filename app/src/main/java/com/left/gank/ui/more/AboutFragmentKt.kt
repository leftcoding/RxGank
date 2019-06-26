package com.left.gank.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.OnClick
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.left.gank.R
import com.left.gank.ui.base.fragment.SupportFragment

/**
 *
 * Create by LingYan on 2019-06-26
 */
class AboutFragmentKt : SupportFragment() {
    @BindView(R.id.about_toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.about_collapsing)
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_about
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolbarLayout!!.apply {
            title = context.getString(R.string.navigation_about)
        }
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    @OnClick(R.id.about_item_text_gank)
    fun clickGank() {
        gotoUrl(context?.getString(R.string.about_gank_url))
    }

    @OnClick(R.id.about_item_text_mzitu)
    fun clickMzitu() {
        gotoUrl(context?.getString(R.string.about_mzitu_url))
    }

    @OnClick(R.id.about_item_text_github)
    fun clickGithub() {
        gotoUrl(context?.getString(R.string.about_my_github_url))
    }

    private fun gotoUrl(url: String?) {
        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}