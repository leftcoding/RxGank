package com.left.gank.ui.ios.text

import android.business.domain.Solid
import android.view.View

interface ItemCallback {
    fun onItemClick(view: View, solid: Solid)
}