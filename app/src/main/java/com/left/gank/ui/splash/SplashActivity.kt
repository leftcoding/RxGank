package com.left.gank.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.left.gank.R
import com.left.gank.domain.PoseCode
import com.left.gank.domain.PoseEvent
import com.left.gank.ui.MainActivity
import com.left.gank.ui.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * Create by LingYan on 2019-06-30
 */
class SplashActivity : BaseActivity() {
    private var permissionSuccess = false

    private val handler: Handler = Handler(Looper.getMainLooper())

    companion object {
        private const val ANIMATE_DURATION = 1000L
        private const val SCALE_X = 1.5f
        private const val SCALE_Y = 1.5f
    }

    override fun getContentId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (permissionSuccess) {
            delayStartAnimate()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(postEvent: PoseEvent) {
        if (postEvent.code == PoseCode.NEED_PERMISSION_SUCCESS) {
            delayStartAnimate()
        }
    }

    private fun delayStartAnimate() {
        handler.post { startAnim() }
    }

    private fun startAnim() {
        splash_img!!.animate()
                .scaleX(SCALE_X)
                .scaleY(SCALE_Y)
                .setDuration(ANIMATE_DURATION)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (isDestroyed) return
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}