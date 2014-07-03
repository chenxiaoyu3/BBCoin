package com.chenxiaoyu.bbcoin;

import android.os.Bundle;


import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class SettingActivity extends SherlockPreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PreferManager.PPEFER_FILE);
		addPreferencesFromResource(R.xml.preference);
	}
	
	@Override
	protected void onDestroy() {
		TimerManager.Instance.restart();
		super.onDestroy();
	}

}
