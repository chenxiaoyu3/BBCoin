package com.chenxiaoyu.bbcoin;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;



import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends FragmentActivity {
	
	static MainActivity instance;
	public static Activity getInstance(){
		return instance;
	}
//	List<CoinStatusFragment> coinsFragments = new ArrayList<CoinStatusFragment>();

	
	// menu 
	LinearLayout menuView;
	List<SingleCoinView> coinsViews;
	Button refreshButton;
	
	FetchPricesTask refreshPricesTask;
	FetchAllTradeListTask fetchAllTradeListTask;
	CoinsPrice coinsPrice;
	Handler handler;
	// menu end
	
	FragmentPagerAdapter fragmentPagerAdapter;
	ViewPager pager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	DataCenter.getInstance();
        super.onCreate(savedInstanceState);
        MainActivity.instance = this;
        setContentView(R.layout.activity_main);

        
//        for (int i = 0; i < Coin.COINS.length; i++) {
//        	CoinStatusFragment c = new CoinStatusFragment(i);
//        	coinsFragments.add(c);
//		}
        
        fragmentPagerAdapter = new CoinsAdapter(getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(fragmentPagerAdapter);
        
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        
        
        SlidingMenu slidingMenu = new SlidingMenu(this);
        
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.layout_slidingmenu);
        
        
        initIDs();
        init();
        
    }
    
    void init(){
    	handler = new Handler(){
    		@Override
    		public void handleMessage(Message arg0) {
    			switch (arg0.what) {
				case 0:
					if (coinsPrice != null) {
						refreshButton.setText(Utils.timePassed(MainActivity.this, coinsPrice.updateTime));
					}
					
					break;

				default:
					break;
				}
    			super.handleMessage(arg0);
    		}
    	};
    	TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
//				for(CoinStatusFragment f : coinsFragments){
//					f.refreshCoinStatus();
//				}
				
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 1000, 5000);
		
		// all coin
		
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
			singleCoinView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			this.menuView.addView(singleCoinView);
			this.coinsViews.add(singleCoinView);
		}
		
		
    }
    
    void initIDs(){
    	this.menuView = (LinearLayout)findViewById(R.id.v_slideMenu_coins);
    	this.refreshButton = (Button)findViewById(R.id.bt_slideMenu_refresh);
    }
    
    void setCoinsPrices(CoinsPrice cp)
    {
    	for(int i = 0; i < Coin.COINS.length; i++){
			SingleCoinView singleCoinView = this.coinsViews.get(i);
			singleCoinView.setCoin(cp.prices.get(i));
		}
    	this.coinsPrice = cp;
    }
    
    CoinStatusFragment findCoinStatusFragment(int coinID){
    	return (CoinStatusFragment)this.fragmentPagerAdapter.getItem(coinID);
    }
    
    class CoinsAdapter extends FragmentPagerAdapter {
        public CoinsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	CoinStatusFragment c = new CoinStatusFragment(position);
            return c;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Coin.COINS[position % Coin.COINS.length];
        }

        @Override
        public int getCount() {
            return Coin.COINS.length;
        }
        
    }
    
    class FetchPricesTask extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchCoinsPrices();
		}
		
		@Override
		protected void onPostExecute(Object arg0) {
			if(arg0 != null){
				setCoinsPrices((CoinsPrice)arg0);
			}		
			super.onPostExecute(arg0);
			refreshPricesTask = null;
		}
    }
    
    
    class FetchAllTradeListTask extends AsyncTask<Object, Object, Object>
    {

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
		}
    	
    }
    

}
