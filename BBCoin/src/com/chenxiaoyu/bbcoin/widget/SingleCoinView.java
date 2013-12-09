package com.chenxiaoyu.bbcoin.widget;


import com.chenxiaoyu.bbcoin.Coin;
import com.chenxiaoyu.bbcoin.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleCoinView extends LinearLayout{

	TextView nameTextView, priceTextView;
    final String TAG = "SingleCoinView";
    public SingleCoinView(Context context)
    {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.layout_singlecoin, this);
            initID();
            init();
            
            
    }
    public SingleCoinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_singlecoin, this);
        initID();
        init();
    }

	private void initID()
	{
		this.priceTextView = (TextView)findViewById(R.id.tv_singlecoin_price);
		this.nameTextView = (TextView)findViewById(R.id.tv_singlecoin_name);
	}
	private void init()
	{

	}
	
	public void setCoin(Coin c)
	{
		this.priceTextView.setText(String.format("%.3f", c.getPrice()));
		this.nameTextView.setText(c.getCoinName());
	}
}
