package com.chenxiaoyu.bbcoin;


import com.chenxiaoyu.bbcoin.service.BBCoinService;
import com.umeng.update.UmengUpdateAgent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class BBCoinApp extends Application{

	public static Context AppContext;
	public static MainActivity MainActivity;
	@Override
	public void onCreate() {
		AppContext = this;
		super.onCreate();
		Intent i = new Intent(this, BBCoinService.class);
		startService(i);
		try {
			org.stockchart.core.Theme.setCurrentThemeFromResources(this, R.raw.dark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PreferManager.Instance.init(this);
		
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}
	
}
