package com.left.gank.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import com.left.gank.R
import com.left.gank.ui.MainActivityKt
import com.left.gank.ui.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 *
 * Create by LingYan on 2019-06-30
 */
class SplashActivityKt : BaseActivity() {
    companion object {
        private const val ANIMATE_DURATION = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delayStartAnimate()
    }

    override fun getContentId(): Int {
        return R.layout.activity_splash
    }

    private fun delayStartAnimate() {
        splash_img.postDelayed({ startAnim() }, 1000)
    }

    private fun startAnim() {
        if (splash_img == null) return
        splash_img.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(ANIMATE_DURATION.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (isDestroyed) return
                        val intent = Intent(this@SplashActivityKt, MainActivityKt::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                .start()
    }
}