package com.left.gank.ui.ios.text

import android.business.domain.Gank
import android.view.View

interface ItemCallback {
    fun onItemClick(view: View, gank: Gank)
}