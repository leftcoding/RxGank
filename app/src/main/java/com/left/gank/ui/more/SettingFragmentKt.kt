package com.left.gank.ui.more

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import com.left.gank.R
import com.left.gank.domain.CheckVersion
import com.left.gank.ui.base.fragment.SupportFragment
import com.left.gank.ui.index.LauncherPresenter
import com.left.gank.utils.AppUtils
import com.left.gank.view.ILauncher
import com.left.gank.widget.ItemTextView

/**
 *
 * Create by LingYan on 2019-06-27
 */
class SettingFragmentKt : SupportFragment(), ILauncher {
    override fun callUpdate(checkVersion: CheckVersion?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun noNewVersion() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hiddenDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @BindView(R.id.setting_item_text_update)
    internal var itemUpdate: ItemTextView? = null

    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null

    private var presenter: LauncherPresenter? = null

    companion object {
        fun newInstance(): SettingFragmentKt {
            return SettingFragmentKt()
        }
    }

    override fun fragmentLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemUpdate!!.apply {
            setTextSummary(itemUpdate!!.context?.getString(R.string.setting_current_version,
                    AppUtils.getVersionName(activity)))
            setTextName(R.string.setting_check_version)
        }

        toolbar!!.apply {
            setTitle(R.string.navigation_settings)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = LauncherPresenter(activity, this);
    }

}