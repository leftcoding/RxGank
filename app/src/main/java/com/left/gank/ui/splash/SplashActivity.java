package com.left.gank.ui.splash;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.ui.MainActivity;
import com.left.gank.ui.base.activity.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-06-01
 */
public class SplashActivity extends BaseActivity {
    private static final int ANIMATE_DURATION = 500;

    @BindView(R.id.splash_img)
    View splashGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delayStartAnimate();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_splash;
    }

    private void delayStartAnimate() {
        splashGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }
        }, 1000);
    }

    private void startAnim() {
        if (splashGroup == null) return;
        splashGroup.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(ANIMATE_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (isDestroyed()) return;
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }
}
