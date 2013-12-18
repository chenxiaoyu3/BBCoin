package com.chenxiaoyu.bbcoin;

import com.chenxiaoyu.bbcoin.service.BBCoinService;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class BBCoinApp extends Application{

	static Context AppContext;
	@Override
	public void onCreate() {
		AppContext = this;
		super.onCreate();
//		Intent i = new Intent(this, BBCoinService.class);
//		startService(i);
	}
	
}
