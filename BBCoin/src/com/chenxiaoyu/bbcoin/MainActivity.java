package com.chenxiaoyu.bbcoin;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;



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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends FragmentActivity {
	
	List<CoinStatusFragment> coinsFragments = new ArrayList<CoinStatusFragment>();

	LinearLayout menuView;
	Button refreshButton;
	FetchPricesTask refreshPricesTask;
	CoinsPrice coinsPrice;
	Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        
        for (String co : Coin.COINS) {
        	CoinStatusFragment c = new CoinStatusFragment(co);
        	coinsFragments.add(c);
		}
        
        FragmentPagerAdapter adapter = new CoinsAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

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
						refreshButton.setText(String.valueOf(Utils.secPassed(coinsPrice.updateTime)));
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
				for(CoinStatusFragment f : coinsFragments){
					f.refreshCoinStatus();
				}
				
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
				
			}
		});
		
		
		
    }
    
    void initIDs(){
    	this.menuView = (LinearLayout)findViewById(R.id.v_slideMenu_coins);
    	this.refreshButton = (Button)findViewById(R.id.bt_slideMenu_refresh);
    }
    
    void setCoinsPrices(CoinsPrice cp)
    {
    	this.menuView.removeAllViews();
    	for(Coin coin : cp.prices){
			SingleCoinView singleCoinView = new SingleCoinView(this);
			singleCoinView.setCoin(coin);
			singleCoinView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			this.menuView.addView(singleCoinView);
		}
    	this.refreshButton.setText(Utils.time2str(cp.updateTime));
    	this.coinsPrice = cp;
    }
    
    class CoinsAdapter extends FragmentPagerAdapter {
        public CoinsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return coinsFragments.get(position);
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
    

}
