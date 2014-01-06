package com.chenxiaoyu.bbcoin;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.stockchart.core.Theme;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.chenxiaoyu.bbcoin.widget.CoinTableView;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;
import com.chenxiaoyu.bbcoin.widget.SplashView;
import com.chenxiaoyu.bbcoin.widget.TradeMarketView;
import com.chenxiaoyu.bbcoin.widget.SplashView.SplashListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;




import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends SherlockActivity implements SplashListener{

	public final String TAG = "MainActivity";
	//------- menu
	SlidingMenu mSlidingMenu;
	ImageButton mSettingButton;
	Button mMenuPriceOverview, mMenuTradeMarket;
	//------end menu
	SplashView mSplashView;
	TradeMarketView mTradeMarketView;
	CoinTableView mCoinTableView;
	ViewGroup mRootView;
	int mCurRightView;
	static final int VIEW_TRADE_MARKET = 1, VIEW_COIN_TABLE = 0;
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			getSupportActionBar().show();
		};
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	BBCoinApp.MainActivity = this;
    	DataCenter.getInstance();
    	getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        initIDs();
        init();        
    }
    void initSlidingMenu(){
    	mSlidingMenu = new SlidingMenu(this);
    	mSlidingMenu.setMode(SlidingMenu.LEFT);
    	mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
    	mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    	mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    	mSlidingMenu.setShadowDrawable(R.drawable.shadow);
    	mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    	mSlidingMenu.setMenu(R.layout.layout_sliding_menu);
        
        mSettingButton = (ImageButton)findViewById(R.id.bt_slideMenu_setting);
        mMenuPriceOverview = (Button)findViewById(R.id.bt_slideMenu_priceOverview);
        mMenuTradeMarket = (Button)findViewById(R.id.bt_slideMenu_tradeMarket);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(i);
			}
		});
        mMenuPriceOverview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpCoinTable();
			}
		});
        mMenuTradeMarket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpTradeMarket();
			}
		});
    }
    @Override
	protected void onDestroy() {

		super.onDestroy();
	}
    void initIDs(){
    	mRootView = (ViewGroup)findViewById(R.id.v_main_root);
    }
    void init(){
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	mSplashView = new SplashView(this);
    	mSplashView.setSplashListener(this);
    	mCoinTableView = new CoinTableView(this);
    	
    	mRootView.addView(mSplashView);
    	mSplashView.playAnimation();
    	
    }
	@Override
	public void onSplashFinish() {
		
//		mRootView.removeView(mSplashView);
		mSplashView.setVisibility(View.GONE);
		getSupportActionBar().show();
		mRootView.addView(mCoinTableView);
		initSlidingMenu();
	}
    void jumpTradeMarket(){
    	
    	if (mCurRightView == VIEW_TRADE_MARKET) {
    		mSlidingMenu.showContent(true);
		}else{
			mSlidingMenu.showContent(false);
			mRootView.removeAllViews();
	    	mCoinTableView = null;
	    	mTradeMarketView = new TradeMarketView(this);
	    	mRootView.addView(mTradeMarketView);
	    	mCurRightView = VIEW_TRADE_MARKET;
		}
    	
    }
    void jumpCoinTable(){
    	
    	if (mCurRightView == VIEW_COIN_TABLE) {
    		mSlidingMenu.showContent(true);
		}else {
			mSlidingMenu.showContent(false);
			mRootView.removeAllViews();
	    	mTradeMarketView = null;
	    	mCoinTableView = new CoinTableView(this);
	    	mRootView.addView(mCoinTableView);
	    	mCurRightView = VIEW_COIN_TABLE;
		}

    }
    public void onCoinBlockClicked(int coinID){
    	jumpTradeMarket();
    	mTradeMarketView.coinViewSwith(coinID);
    }

    public void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

    //------------------------------- Adapter ---------------------------------------
    
    
   //--------------------------------------------Task---------------------------------------
 
    

}
