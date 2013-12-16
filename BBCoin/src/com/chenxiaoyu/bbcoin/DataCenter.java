package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;

import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.model.Order;

public class DataCenter {


	
	
	List<CoinStatus> mAllCoinStatus;
	
	public DataCenter() {
		mAllCoinStatus = new ArrayList<CoinStatus>();
		mCoinsPrice = new CoinsPrice();
		mCoinsPrice.prices = new ArrayList<Coin>();
		for(int i = 0; i < Coin.COINS.length; i++){
			CoinStatus cs = new CoinStatus();
			cs.setCoinID(i);
			cs.sellOrders = new ArrayList<Order>();
			cs.buyOrders = new ArrayList<Order>();
			mAllCoinStatus.add(cs);
			
			Coin c = new Coin();
			c.setCoin(i);
			c.setPrice(0);
			mCoinsPrice.prices.add(c);
		}
		
	}
	
	public List<CoinStatus> getAllCoinStatus() {
		return mAllCoinStatus;
	}
	
	public void updateTradeList(List<CoinStatus> data){
		for (CoinStatus coinStatus : data) {
			CoinStatus cs = mAllCoinStatus.get( coinStatus.getCoinID() );
			cs.buyOrders.clear();
			cs.buyOrders.addAll(coinStatus.buyOrders);
			cs.updateTime = coinStatus.updateTime;
			cs.sellOrders.clear();
			cs.sellOrders.addAll(coinStatus.sellOrders);
		}
	}
	
	CoinsPrice mCoinsPrice;
	public CoinsPrice getCoinsPrice() {
		return mCoinsPrice;
	}
	public void updateCoinsPrice(CoinsPrice cp){
		mCoinsPrice.updateTime = cp.updateTime;
		for(int i = 0; i < Coin.COINS.length; i++){
			mCoinsPrice.prices.get(i).setPrice(cp.prices.get(i).getPrice());
		}
	}
	//------- global data
	Map<String, Object> globalObj = new HashMap<String, Object>();
	public void pushGlobalObj(String key,Object obj){
		globalObj.put(key, obj);
	}
	public Object fetchGlobalObj(String key){
		Object ret = globalObj.get(key);
		globalObj.remove(key);
		return ret;
	}
	//------ end global data
	
	
	
	
	
	
	
	static DataCenter instance;
	public static DataCenter getInstance(){
		if(instance == null){
			instance = new DataCenter();
		}
		return instance;
	}
	
	
	
}
