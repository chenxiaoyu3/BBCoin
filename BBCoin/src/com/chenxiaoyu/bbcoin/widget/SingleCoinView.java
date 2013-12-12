package com.chenxiaoyu.bbcoin.widget;


import com.chenxiaoyu.bbcoin.Coin;
import com.chenxiaoyu.bbcoin.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleCoinView extends LinearLayout{

	TextView nameTextView, priceTextView;
    final String TAG = "SingleCoinView";
    Coin coin;
    Context context;
    public SingleCoinView(Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_singlecoin, this);
        initID();
        init();
        this.context = context;
    }
//    public SingleCoinView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.layout_singlecoin, this);
//        initID();
//        init();
//        this.context = context;
//    }

	private void initID()
	{
		this.priceTextView = (TextView)findViewById(R.id.tv_singlecoin_price);
		this.nameTextView = (TextView)findViewById(R.id.tv_singlecoin_name);
	}
	private void init()
	{

	}
	
	 void setColor(int color){
		 this.priceTextView.setTextColor(color);
		 this.nameTextView.setTextColor(color);
	 }
	public void setCoin(Coin c)
	{
		this.priceTextView.setText(String.format("%.3f", c.getPrice()));
		this.nameTextView.setText(c.getCoinName());
		if (this.coin != null ) {
			if(this.coin.getPrice() < c.getPrice()){
				this.setColor(Color.GREEN);
			}else if(this.coin.getPrice() > c.getPrice()){
				this.setColor(Color.RED);
			}else{
				this.setColor(Color.WHITE);
			}
		}
		this.coin = c;
	}
}
