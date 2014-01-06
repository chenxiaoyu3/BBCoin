package com.chenxiaoyu.bbcoin.widget;

import com.chenxiaoyu.bbcoin.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashView extends RelativeLayout{

	Context mContext;
	ImageView mImageView;
	SplashListener mSplashListener;
	
	public SplashView(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_splash, this);
		mImageView = (ImageView)findViewById(R.id.iv_splash);
	}
	
	public void playAnimation(){

		Animation ani = AnimationUtils.loadAnimation(mContext, R.anim.image_scale);
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
				if (mSplashListener != null) {
					mSplashListener.onSplashFinish();
				}
				
			}
		});
		mImageView.startAnimation(ani);
		
	}
	public void setSplashListener(SplashListener sspListener){
		mSplashListener = sspListener;
	}
	public interface SplashListener{
//		void onSplashGoingFinish();
		void onSplashFinish();
	}
}
