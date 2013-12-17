package com.chenxiaoyu.bbcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public enum PreferenceManager {
	Instance;
	
	private final String PREFR_ALARM = "BBCoin_Alarm";
	PreferenceManager() {
		  
	}
	
	public void set(Context context, String key, Object value){
		SharedPreferences sp = context.getSharedPreferences(PREFR_ALARM, Context.MODE_MULTI_PROCESS);
		Editor editor = sp.edit();
		if (value instanceof Integer) {
			editor.putInt(key, (Integer)value);
		}else if(value instanceof String){
			editor.putString(key, (String)value);
		}else if(value instanceof Float){
			editor.putFloat(key,(Float)value);
		}
		editor.commit();
	}
	
	public Object get(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PREFR_ALARM, Context.MODE_MULTI_PROCESS);
		return sp.getAll().get(key);
	}

	
}
