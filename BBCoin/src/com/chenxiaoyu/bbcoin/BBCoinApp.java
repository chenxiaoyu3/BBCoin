package com.chenxiaoyu.bbcoin;


import com.chenxiaoyu.bbcoin.service.BBCoinService;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class BBCoinApp extends Application{

	public static Context AppContext;
	public static MainActivity MainActivity;
	public static BBCoinApp Application;
	@Override
	public void onCreate() {
		AppContext = this;
		Application = this;
		super.onCreate();
		Intent i = new Intent(this, BBCoinService.class);
		startService(i);
		try {
			org.stockchart.core.Theme.setCurrentThemeFromResources(this, R.raw.dark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PreferManager.Instance().init(this);
		initUmeng();
	}
	
	private void initUmeng(){
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			
			@Override
			public void onClick(int arg0) {
				switch (arg0) {
			        case com.umeng.update.UpdateStatus.Update:
			            break;
			        case com.umeng.update.UpdateStatus.NotNow:
			        	closeApp();
			        default:
//			            closeApp();
			        }
			}
		});
	}
	
	public void closeApp(){
		if (MainActivity != null) {
//			MainActivity.finish();
			System.exit(0);
		}
	}
	
	public String getVersionName(){
		String ret = "v";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			ret += pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
