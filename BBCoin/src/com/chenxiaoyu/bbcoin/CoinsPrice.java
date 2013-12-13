package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

public class CoinsPrice {
	public Calendar updateTime;
	public List<Coin> prices;
	
	public static CoinsPrice parseJSON(String str){
		CoinsPrice ret = null;
		try {
			ret = new CoinsPrice();
			ret.prices = new ArrayList<Coin>();
			JSONObject jsonObject = new JSONObject(str); 
			for( int i = 0 ; i < Coin.COINS.length; i++) {
				
				String keyString = Coin.COINS[i];
				double price = jsonObject.getDouble(keyString);
				Coin c = new Coin();
				c.setCoin(i);
				c.setPrice(price);
				ret.prices.add(c);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(jsonObject.getLong("updatetime")*1000);
			ret.updateTime = calendar;

		} catch (Exception e) {
			ret = null;
			e.printStackTrace();
		}
		return ret;
	}

}
