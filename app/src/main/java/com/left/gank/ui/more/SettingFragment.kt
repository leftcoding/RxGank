package com.left.gank.ui.more

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.left.gank.R
import com.left.gank.config.Preferences
import com.left.gank.domain.CheckVersion
import com.left.gank.listener.DialogOnClick
import com.left.gank.ui.base.fragment.SupportFragment
import com.left.gank.ui.download.DownloadPresenter
import com.left.gank.utils.AppUtils
import com.left.gank.utils.GanklyPreferences
import com.left.gank.utils.GlideCatchUtil
import com.left.gank.utils.ToastUtils
import com.left.gank.view.ILauncher
import com.left.gank.widget.UpdateVersionDialog
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.layout_bar.*

/**
 * 设置
 * Create by LingYan on 2016-05-10
 */
class SettingFragment : SupportFragment(), ILauncher {
    private lateinit var downloadPresenter: DownloadPresenter
    private lateinit var versionDialog: UpdateVersionDialog
    private lateinit var progressDialog: ProgressDialog

    override fun fragmentLayoutId(): Int = R.layout.fragment_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreferences()

        (activity as MoreActivity).apply {
            setTitle(R.string.navigation_settings)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        toolbar.apply {
            setNavigationOnClickListener {
                activity!!.onBackPressed()
            }
        }

        val cacheSize = GlideCatchUtil.getInstance().getCacheSize(context)
        setting_item_text_clean_cache.setTextSummary(context!!.getString(R.string.setting_picture_cache, cacheSize))
        setting_switch_check.setSwitchListener { isCheck -> GanklyPreferences.putBoolean(context, Preferences.SETTING_AUTO_CHECK, isCheck) }
        setting_switch_only_wifi.setSwitchListener { isCheck -> GanklyPreferences.putBoolean(context, Preferences.SETTING_WIFI_ONLY, isCheck) }
        setting_rl_about.setOnClickListener {
            (activity as MoreActivity).apply {
                addHideFragment(this@SettingFragment, AboutFragment(), SettingFragment::class.java.name, R.id.setting_frame_layout)
            }
        }
        setting_item_text_update.setOnClickListener {
            Beta.checkUpgrade()
        }
        setting_item_text_clean_cache.setOnClickListener {
            GlideCatchUtil.getInstance().clearCacheDiskSelf(context)
            setting_item_text_clean_cache!!.setTextSummary(context!!.getString(R.string.setting_picture_cache_string))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        downloadPresenter = DownloadPresenter(context!!, this)
    }

    private fun initPreferences() {
        setting_item_text_update.apply {
            val summary = context!!.getString(R.string.setting_current_version,
                    AppUtils.getVersionName(context))
            setTextSummary(summary)
            setTextName(R.string.setting_check_version)
        }

        setting_switch_check.apply {
            val isAutoCheck = GanklyPreferences.getBoolean(context, Preferences.SETTING_AUTO_CHECK, true)
            setSwitchChecked(isAutoCheck)
        }
        setting_switch_only_wifi.apply {
            val isOnlyWifi = GanklyPreferences.getBoolean(context, Preferences.SETTING_WIFI_ONLY, false)
            setSwitchChecked(isOnlyWifi)
        }
    }

    override fun callUpdate(checkVersion: CheckVersion) {
        showVersionDialog(checkVersion.changelog)
    }

    override fun noNewVersion() {
        ToastUtils.showToast(context, R.string.tip_no_new_version)
    }

    private fun showVersionDialog(content: String) {
        if (!::versionDialog.isInitialized) {
            versionDialog = UpdateVersionDialog()
        }
        versionDialog.setDialogOnClick(object : DialogOnClick {
            override fun cancel() {
                versionDialog.dismiss()
            }

            override fun submit() {
                versionDialog.dismiss()
                ToastUtils.showToast(context, R.string.update_downing)
                downloadPresenter.downloadApk()
            }
        })

        val bundle = Bundle()
        bundle.putString(UpdateVersionDialog.UPDATE_CONTENT, content)
        versionDialog.arguments = bundle
        versionDialog.show(activity!!.supportFragmentManager, DIALOG_TAG)
    }

    private fun createDialog() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgressDialog(activity)
        }
        progressDialog.setMessage(context!!.getString(R.string.dialog_checking))
        progressDialog.show()
    }

    private fun disDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun showDialog() {
        createDialog()
    }

    override fun hiddenDialog() {
        disDialog()
    }

    companion object {
        private const val DIALOG_TAG = "versionDialog"

        fun newInstance(): SettingFragment = SettingFragment()
    }
}
