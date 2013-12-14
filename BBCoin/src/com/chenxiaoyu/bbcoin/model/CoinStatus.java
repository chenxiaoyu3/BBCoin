package com.chenxiaoyu.bbcoin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class CoinStatus {

	int coinID;
	public List<Order> buyOrders, sellOrders;
	public Date updateTime;
	public CoinStatus() {
		
	}
	
	public static CoinStatus parseJOSN(JSONObject jsonObject)
	{
		CoinStatus ret = new CoinStatus();
        try {       
            List<Order> buyOrders = parseOrders(jsonObject.getJSONArray("buyOrder"), Order.BUY);
            List<Order> sellOrders = parseOrders(jsonObject.getJSONArray("sellOrder"), Order.SELL);
            ret.buyOrders = buyOrders;
            ret.sellOrders = sellOrders;
        } catch (JSONException e) {
            e.printStackTrace();
            ret = null;
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
	
	public static void Overlay(CoinStatus beOverlayed, CoinStatus toOverlay)
	{
		beOverlayed.buyOrders.clear();
		beOverlayed.buyOrders.addAll(toOverlay.buyOrders);
		beOverlayed.sellOrders.clear();
		beOverlayed.sellOrders.addAll(toOverlay.sellOrders);
		
	}

	public int getCoinID() {
		return coinID;
	}

	public void setCoinID(int coinID) {
		this.coinID = coinID;
	}
}
