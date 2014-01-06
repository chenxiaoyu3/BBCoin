package com.chenxiaoyu.bbcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public enum PreferManager {
	Instance;
	
	PreferManager() {
		  //"auto_refresh_delta"
	}
	
	public void set(Context context, String key, Object value){
		SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
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
		SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getAll().get(key);
	}
	
	public void init(Context context){
		if (get(context, "auto_refresh_delta") == null) {
			set(context, "auto_refresh_delta", "300");
		}
	}

	
}
