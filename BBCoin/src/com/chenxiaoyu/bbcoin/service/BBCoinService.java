
package com.chenxiaoyu.bbcoin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.model.PriceAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class BBCoinService extends Service {

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
			}, 10000, 10*60*1000);
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	public void doNotify( int coinID, String title, String content, String ticker){
		int flag = Notification.DEFAULT_ALL;
//		if (mNotificationsFlag[coinID]) {
//			flag = Notification.DEFAULT_LIGHTS;
//		}
		Notification n = new NotificationCompat.Builder(BBCoinService.this)
    	.setContentTitle(title)
    	.setContentText(content)
    	.setAutoCancel(false)
    	.setTicker(ticker)
    	.setDefaults(flag)
    	.setSmallIcon(R.drawable.ic_bitcoin)
    	.setOnlyAlertOnce(true)
    	.build();
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	nm.notify(coinID, n);
    	mNotificationsFlag[coinID] = true;
	}
	
	class FetchPricesTask extends AsyncTask<Object, Object, Object>{

    	@Override
    	protected void onPreExecute() {
    		Log.v(TAG, "Fetch begin");
    		super.onPreExecute();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchCoinsBuyPrice();
		}
		
		@Override
		protected void onPostExecute(Object arg0) {
			if(arg0 != null){
				CoinsPrice cp = (CoinsPrice)arg0;
				for(int i = 0; i < Coin.COINS.length; i++){
					PriceAlarm a = AlarmManager.Instance.getPriceAlarm(BBCoinService.this, i);
					if (a != null) {
			        	if( a.lessThan > 0 && a.lessThan >= cp.prices.get(i).getPrice()){
			        		String ti = Coin.sGetStrName(i) + " < " + a.lessThan;
			        		String content = Coin.sGetStrName(i) + " : гд" + cp.prices.get(i).getPrice();
				        	doNotify(i, ti, content, "!!! " + ti);
			        	}
			        	if ( a.largerThan > 0 && a.largerThan <= cp.prices.get(i).getPrice()) {
			        		String ti = Coin.sGetStrName(i) + " > " + a.largerThan;
			        		String content = Coin.sGetStrName(i) + " : гд" + cp.prices.get(i).getPrice();
				        	doNotify(i, ti, content, "!!! " + ti);
						}
					}
				}
			}		
			super.onPostExecute(arg0);
			mFetchPricesTask = null;
		}
    }
} 