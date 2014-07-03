
package com.chenxiaoyu.bbcoin.service;


import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


public class BBCoinService extends Service {

	public static final String REQ_CANCEL_ALARM = "REQ_CANCEL_ALARM";
	public static final String TAG = "BBCoinService";
	
	FetchPricesTask mFetchPricesTask;
	Timer mTimer;
	boolean[] mNotificationsFlag;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) {
			case 0:
				if (mFetchPricesTask == null) {
					mFetchPricesTask = new FetchPricesTask();
					mFetchPricesTask.execute(0);
				}
				break;

			default:
				break;
			}
		};
	};
	public IBinder onBind(Intent intent) { 
		return null; 
	} 
	@Override 
	public void onCreate() { 
		super.onCreate();
		init();
		Log.v(TAG, "onCreate");
	}
	public void init(){
		mNotificationsFlag = new boolean[Coin.COINS.length];
		for(int i = 0; i < mNotificationsFlag.length; i++){
			mNotificationsFlag[i] = false;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.v(TAG, "onStart");
		try {
			int cid = intent.getIntExtra(REQ_CANCEL_ALARM, -1);
			Log.v(TAG, "Cancel alarm: " + cid);
			if ( cid != -1) {
				// this intent is from cancel request
				AlarmManager.Instance.setPriceAlarm(BBCoinService.this, cid, 0, 0);
				AlarmManager.Instance.cancelNotification(this, cid);
			}else {
				if (mTimer != null) {
					mTimer.cancel();
				}
				if (AlarmManager.Instance.needAlarm(this)) {
					mTimer = new Timer();
					mTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							Log.v(TAG, "task run!");
							mHandler.sendEmptyMessage(0);
							
						}
					}, 10000, 15*60*1000);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	public static void NeedStart(Context ctx){
		Intent intent = new Intent(ctx, BBCoinService.class);
		ctx.startService(intent);
	}


	class FetchPricesTask extends AsyncTask<Object, Object, Object>{

    	@Override
    	protected void onPreExecute() {
//    		Log.v(TAG, "Fetch begin");
    		super.onPreExecute();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchCoinsBuyPrice();
		}
		
		@Override
		protected void onPostExecute(Object arg0) {
			if(arg0 != null){
				AlarmManager.Instance.doCheckAndAlarm(BBCoinService.this, (CoinsPrice)arg0);
			}		
			super.onPostExecute(arg0);
			mFetchPricesTask = null;
		}
    }
} 