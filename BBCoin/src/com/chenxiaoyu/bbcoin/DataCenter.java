package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {


	static DataCenter instance;
	public static DataCenter getInstance(){
		if(instance == null){
			instance = new DataCenter();
		}
		return instance;
	}
	
	List<CoinStatus> mAllCoinStatus = new ArrayList<CoinStatus>();
	
	public DataCenter() {
		
		for(int i = 0; i < Coin.COINS.length; i++){
			CoinStatus cs = new CoinStatus();
			cs.setCoinID(i);
			cs.sellOrders = new ArrayList<Order>();
			cs.buyOrders = new ArrayList<Order>();
			mAllCoinStatus.add(cs);
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
			cs.sellOrders.clear();
			cs.sellOrders.addAll(coinStatus.sellOrders);
		}
	}
	
	
	
	
	
	
	
}
