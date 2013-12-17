package com.chenxiaoyu.bbcoin;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.stockchart.core.Theme;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;



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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends SherlockFragmentActivity {

	public final String TAG = "MainActivity";
	//------- menu
	LinearLayout mCoinsView;
	List<SingleCoinView> coinsViews;
	Button refreshButton;
	SlidingMenu slidingMenu;
	TextView timestampTextView;
	ProgressBar mLoadingPriceBar, mLoadingStatusBar;
	//------end menu
	FragmentPagerAdapter fragmentPagerAdapter;
	ViewPager pager;
	TabPageIndicator indicator;
	
	Button chartButton;
	Timer timer;
	FetchPricesTask refreshPricesTask;
	FetchAllTradeListTask fetchAllTradeListTask;
	CoinsPrice coinsPrice;
	Date lastUpdate = null;

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message arg0) {
			switch (arg0.what) {
			case 0:
				if (lastUpdate != null) {
					refreshButton.setText(Utils.timePassed(MainActivity.this, lastUpdate) + getString(R.string.passed));
				}
				
				break;

			default:
				break;
			}
			super.handleMessage(arg0);
		}
	};;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	DataCenter.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlidingMenu();
        initIDs();
        
        fragmentPagerAdapter = new CoinsAdapter(getSupportFragmentManager());
        
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(13);
        
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
       
        init();
        
    }
    
    @Override
	protected void onDestroy() {
		timer.cancel();
		
		super.onDestroy();
	}
    void initIDs(){
    	
    	this.refreshButton = (Button)findViewById(R.id.bt_slideMenu_refresh);
    	this.pager = (ViewPager)findViewById(R.id.pager);
    	this.chartButton = (Button)findViewById(R.id.bt_coinstatus_chart);
    	this.mLoadingPriceBar = (ProgressBar)findViewById(R.id.pb_slideMenu_loading);
    	this.mLoadingStatusBar = (ProgressBar)findViewById(R.id.pb_main_loading);
    }
    void init(){
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    
    	TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
//				for(CoinStatusFragment f : coinsFragments){
//					f.refreshCoinStatus();
//				}
				
			}
		};
		timer = new Timer();
		timer.schedule(task, 1000, 5000);
		TimerTask updateTime = new TimerTask() {
			
			@Override
			public void run() {
				
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(updateTime, 1000, 1000);
		this.refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (refreshPricesTask == null) {
					refreshPricesTask = new FetchPricesTask();
					refreshPricesTask.execute(0);
				}
				if (fetchAllTradeListTask == null){
					fetchAllTradeListTask = new FetchAllTradeListTask();
					fetchAllTradeListTask.execute(0);
				}
			}
		});
		
		this.coinsViews = new ArrayList<SingleCoinView>();
		for(int i = 0; i < Coin.COINS.length; i++){
			SingleCoinView singleCoinView = new SingleCoinView(this);
			singleCoinView.setCoinID(i);
			singleCoinView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			this.mCoinsView.addView(singleCoinView);
			this.coinsViews.add(singleCoinView);
		}
		
		this.indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				int cur = pager.getCurrentItem();
				CoinStatusFragment csf = (CoinStatusFragment)fragmentPagerAdapter.getItem( cur );
				csf.doRefresh();
				if (arg0 == 0) {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
				
				@Override
				public void onOpened() {
					if (refreshPricesTask == null) {
						refreshPricesTask = new FetchPricesTask();
						refreshPricesTask.execute(0);
					}
					if (fetchAllTradeListTask == null){
						fetchAllTradeListTask = new FetchAllTradeListTask();
						fetchAllTradeListTask.execute(0);
					}
				
			}
		});
		
		this.chartButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int curCoin = pager.getCurrentItem();
				Intent intent = new Intent(MainActivity.this, KChartActivity.class);
				intent.putExtra("coinID", curCoin);
				MainActivity.this.startActivity(intent);
			}
		});
		try {
			Theme.setCurrentThemeFromResources(this, R.raw.dark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreferenceManager.Instance.set(this, "a", 1);
		int s = (Integer)PreferenceManager.Instance.get(this, "a");
		Toast.makeText(this, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }
    
    
    
    void initSlidingMenu(){
    	slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.layout_slidingmenu);
        
        this.mCoinsView = (LinearLayout)findViewById(R.id.v_slideMenu_coins);
        this.timestampTextView = (TextView)findViewById(R.id.tv_slideMenu_timestamp);
    }
    

    //------------------------------- Adapter ---------------------------------------
    class CoinsAdapter extends FragmentPagerAdapter {
    	CoinStatusFragment[] fragments  = new CoinStatusFragment[Coin.COINS.length];
        public CoinsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	position = position % Coin.COINS.length;
        	if (fragments[position] == null) {
        		CoinStatusFragment c = new CoinStatusFragment();
        		c.setCoinID(position);
        		fragments[position] = c;
//        		Log.v(TAG, "New fragment " + position);
			}
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Coin.COINS[position % Coin.COINS.length];
        }

        @Override
        public int getCount() {
            return Coin.COINS.length;
        }
        
        public void refreshAll(){
        	for (CoinStatusFragment fragment : fragments) {
				fragment.doRefresh();
			}
        }
        
        
    }
    
   //--------------------------------------------Task---------------------------------------
    class FetchPricesTask extends AsyncTask<Object, Object, Object>{

    	@Override
    	protected void onPreExecute() {
    		mLoadingPriceBar.setVisibility(View.VISIBLE);
    		super.onPreExecute();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchCoinsBuyPrice();
		}
		
		@Override
		protected void onPostExecute(Object arg0) {
			DataCenter.getInstance().updateCoinsPrice((CoinsPrice)arg0);
			if(arg0 != null){
				for(SingleCoinView view : coinsViews){
					view.doRefresh();
				}
				timestampTextView.setText( Utils.timeFormat( ((CoinsPrice)arg0).updateTime.getTime(), "hh:mm:ss")  );
				lastUpdate = new Date();
			}		
			super.onPostExecute(arg0);
			mLoadingPriceBar.setVisibility(View.INVISIBLE);
			refreshPricesTask = null;
		}
    }
    
    
    class FetchAllTradeListTask extends AsyncTask<Object, Object, Object>
    {

    	@Override
    	protected void onPreExecute() {
    		mLoadingStatusBar.setVisibility(View.VISIBLE);
    		super.onPreExecute();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchAllTradeList();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				DataCenter.getInstance().updateTradeList((List<CoinStatus>)result);
				int cur = pager.getCurrentItem();
				CoinStatusFragment csf = (CoinStatusFragment)fragmentPagerAdapter.getItem( cur );
				csf.doRefresh();
				
			}
			super.onPostExecute(result);
			fetchAllTradeListTask = null;
			mLoadingStatusBar.setVisibility(View.INVISIBLE);
		}
    	
    }
    

}
