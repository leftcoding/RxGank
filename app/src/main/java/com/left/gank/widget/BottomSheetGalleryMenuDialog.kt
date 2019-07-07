package com.left.gank.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.left.gank.R

/**
 *
 * Create by LingYan on 2019-07-04
 */
open class BottomSheetGalleryMenuDialog(context: Context) : BottomSheetDialog(context) {
    private var save: View? = null
    private var share: View? = null
    private var cancel: View? = null
    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private var callback: Callback? = null

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_gallery_menu, null)
        bottomSheetDialog.setContentView(root)
        save = root.findViewById(R.id.save)
        share = root.findViewById(R.id.share)
        cancel = root.findViewById(R.id.cancel)

        save!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callback?.save()
        }

        share!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            callback?.share()
        }

        cancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    open fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun show() {
        if (!bottomSheetDialog.isShowing) {
            bottomSheetDialog.show()
        }
    }

    interface Callback {
        fun share()

        fun save();
    }
}

