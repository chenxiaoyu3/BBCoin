package com.chenxiaoyu.bbcoin;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.viewpagerindicator.TabPageIndicator;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TabHost.OnTabChangeListener;


public class MainActivity extends FragmentActivity {
	
	List<CoinStatusFragment> coinsFragments = new ArrayList<CoinStatusFragment>();

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
        
        init();
        
    }
    
    void init(){
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
    

}
