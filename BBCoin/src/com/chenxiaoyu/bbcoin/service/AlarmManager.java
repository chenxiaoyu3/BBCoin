package com.chenxiaoyu.bbcoin.service;

import java.util.ArrayList;
import java.util.List;

import com.chenxiaoyu.bbcoin.PreferenceManager;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.PriceAlarm;

import android.content.Context;

public enum AlarmManager {
	Instance;
	
	AlarmManager() {
		  
	}
	/**
	 * 
	 * @param coinID
	 * @param lessThan set 0 if want it clear
	 * @param lagerThan
	 */
	public void setPriceAlarm(Context context, int coinID, float lessThan, float largerThan){
		PreferenceManager.Instance.set(context, coinID + "_price_lessthan", lessThan);
		PreferenceManager.Instance.set(context, coinID + "_price_largerthan", largerThan);
	}
	
	public PriceAlarm getPriceAlarm(Context context, int coinID){
		PriceAlarm ret = null;
		Float a = (Float)PreferenceManager.Instance.get(context, coinID + "_price_lessthan");
		Float b = (Float)PreferenceManager.Instance.get(context, coinID + "_price_largerthan");
		if (a != null && b != null) {
			ret = new PriceAlarm();
			ret.coinID = coinID;
			ret.lessThan = a;
			ret.largerThan = b;
		}
		return ret;
	}
	
	public boolean needAlarm(Context context){
		for(int i = 0; i < Coin.COINS.length; i++){
			PriceAlarm pa = getPriceAlarm(context, i);
			if (pa != null &&(pa.lessThan != 0 || pa.largerThan != 0)) {
				return true;
			}
		}
		return false;
	}
	
}
