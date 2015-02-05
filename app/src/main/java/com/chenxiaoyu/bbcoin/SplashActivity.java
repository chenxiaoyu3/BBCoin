package com.chenxiaoyu.bbcoin;


import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;


public class SplashActivity extends SherlockActivity {

    ImageView mImageView;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        mImageView = (ImageView) findViewById(R.id.iv_splash);
        playAnimation();
    }

    public void playAnimation() {

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.image_scale);
        ani.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageView.setVisibility(View.GONE);
                finish();
            }
        });
        mImageView.startAnimation(ani);

    }
}
