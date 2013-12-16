package com.chenxiaoyu.bbcoin.model;


public class Order{
	
	public static final int BUY = 0;
	public static final int SELL = 1;
	
	public int type;
	public double amount, price, sum;
	
	
	public Order() {
		
	}
	public Order(double price, double amount, int ty) {
		this.price = price;
		this.amount = amount;
		this.sum = this.price * this.amount;
		this.type = ty;
		
	}

}
