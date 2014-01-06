package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.model.OnDataCenterUpdate;
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
	
	public synchronized void updateTradeList(List<CoinStatus> data){
		for (CoinStatus coinStatus : data) {
			CoinStatus cs = mAllCoinStatus.get( coinStatus.getCoinID() );
			cs.buyOrders.clear();
			cs.buyOrders.addAll(coinStatus.buyOrders);
			cs.updateTime = coinStatus.updateTime;
			cs.sellOrders.clear();
			cs.sellOrders.addAll(coinStatus.sellOrders);
		}
		for(OnDataCenterUpdate lis : mToNotify){
			lis.onTradeListUpdate();
		}
	}
	
	public synchronized void updateSingleTrade(CoinStatus coinStatus){
		CoinStatus cs = mAllCoinStatus.get( coinStatus.getCoinID() );
		cs.buyOrders.clear();
		cs.buyOrders.addAll(coinStatus.buyOrders);
		cs.updateTime = coinStatus.updateTime;
		cs.sellOrders.clear();
		cs.sellOrders.addAll(coinStatus.sellOrders);
		for(OnDataCenterUpdate lis : mToNotify){
			lis.onTradeListUpdate();
		}
	}
	
	CoinsPrice mCoinsPrice;
	public CoinsPrice getCoinsPrice() {
		return mCoinsPrice;
	}
	public synchronized void updateCoinsPrice(CoinsPrice cp){
		mCoinsPrice.updateTime = cp.updateTime;
		for(int i = 0; i < Coin.COINS.length; i++){
			mCoinsPrice.prices.get(i).setPrice(cp.prices.get(i).getPrice());
		}
		for(OnDataCenterUpdate lis : mToNotify){
			lis.onPriceUpdate();
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
	
	List<OnDataCenterUpdate> mToNotify = new ArrayList<OnDataCenterUpdate>();
	public void addListener(OnDataCenterUpdate listener){
		if (listener == null) {
			return;
		}
		mToNotify.add(listener);
	}
	public void removeListener(OnDataCenterUpdate listener){
		if (listener == null) {
			return;
		}
		mToNotify.remove(listener);
	}
	
	
}
