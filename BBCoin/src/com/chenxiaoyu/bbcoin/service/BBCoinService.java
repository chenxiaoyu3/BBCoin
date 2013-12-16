
package com.chenxiaoyu.bbcoin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BBCoinService extends Service {

	public static final String TAG = "BBCoinService";
	public IBinder onBind(Intent intent) { 
		return null; 
	} 
	@Override 
	public void onCreate() { 
		 
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return super.onStartCommand(intent, flags, startId);
	}

} 