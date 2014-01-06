package com.chenxiaoyu.bbcoin.model;

public class Coin {

	public static final String [] COINS = {"BTC", "LTC", "XPM", "BEC", "XRP", "ZCC", "MEC", "ANC", "PPC", "SRC", "TAG", "PTS", "WDC","APC","DGC", "TMC"};
	public static final String [] COINS_NAME = {"比特币","莱特币","质数币","比奥币","瑞波币","招财币","美卡币","阿侬币","点点币","安全币","悬赏币","比特股","世界币","苹果币","数码币","时代币"};
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
	public static String getStrNameWithChs(int i){
		return COINS_NAME[i] + COINS[i];
	}
	public static int GetCoinID(String str){
		for(int i = 0; i < Coin.COINS.length; i++){
			if (Coin.COINS[i].equals(str.toUpperCase())) {
				return i;
			}
		}
		return 0;
	}
}
