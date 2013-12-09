package com.chenxiaoyu.bbcoin;

public class Coin {

	public static final String [] COINS = {"BTC", "LTC", "XPM", "BEC", "XRP", "ZCC", "MEC", "ANC", "PPC", "SRC", "TAG", "PTS", "TMC"};
	
	int coin = 0;
	double price;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public int getCoin() {
		return coin;
	}
	
	public String getCoinName()
	{
		return sGetStrName(coin);
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	
	
	
	
	
	public static String sGetStrName(int i){
		return COINS[i];
	}
}
