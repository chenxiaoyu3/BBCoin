package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class CoinsPrice {
	public Date updateTime;
	public List<Coin> prices;
	
	public static CoinsPrice parseJSON(String str){
		CoinsPrice ret = null;
		try {
			ret = new CoinsPrice();
			ret.prices = new ArrayList<Coin>();
			JSONObject jsonObject = new JSONObject(str); 
			for( int i = 0 ; i < Coin.COINS.length; i++) {
				
				String keyString = Coin.COINS[i].toLowerCase() + "2cny";
				double price = jsonObject.getDouble(keyString);
				Coin c = new Coin();
				c.setCoin(i);
				c.setPrice(price);
				ret.prices.add(c);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(jsonObject.getLong("updatetime"));
			ret.updateTime = calendar.getTime();

		} catch (Exception e) {
			// TODO: handle exception
			ret = null;
			e.printStackTrace();
		}
		return ret;
	}

}