package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class CoinStatus {

	public List<Order> buyOrders, sellOrders;
	
	public CoinStatus() {
		
	}
	
	public static CoinStatus parseJSON(String str)
	{
		CoinStatus ret = new CoinStatus();
        try {
            JSONObject jsonObject = new JSONObject(str);         
            List<Order> buyOrders = parseOrders(jsonObject.getJSONArray("buyOrder"), Order.BUY);
            List<Order> sellOrders = parseOrders(jsonObject.getJSONArray("sellOrder"), Order.SELL);
            ret.buyOrders = buyOrders;
            ret.sellOrders = sellOrders;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
	}
	static List<Order> parseOrders(JSONArray array, int type) throws JSONException{
		List<Order> retList = new ArrayList<Order>();
		for(int i = 0; i < array.length(); i++) {
		    JSONObject personObject = array.getJSONObject(i);  
			double price = personObject.getDouble("price");                  
			double amt = personObject.getDouble("amount");
			Order order = new Order(price,amt, type);
		    retList.add(order);      
		 }
		return retList;
	}
}
