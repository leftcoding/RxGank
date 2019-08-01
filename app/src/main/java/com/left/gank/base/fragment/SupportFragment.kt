package com.left.gank.base.fragment

import com.left.gank.utils.ToastUtils

/**
 * Create by LingYan on 2017-09-28
 */

abstract class SupportFragment : ButterKnifeFragment() {
    override fun shortToast(msg: String) {
        if (!isDetached) {
            ToastUtils.showToast(context, msg)
        }
    }
}
