package com.chenxiaoyu.bbcoin.widget;

import com.chenxiaoyu.bbcoin.Order;
import com.chenxiaoyu.bbcoin.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleOrderView extends LinearLayout{

	Order order;
	Context context;
	TextView tvAmount, tvPrice, tvSum;
    final String TAG = "CascadeItemView";
    public SingleOrderView(Context context)
    {
            super(context);
            this.context = context;
            LayoutInflater.from(context).inflate(R.layout.layout_singleorder, this);
            initID();
            init();
            
            
    }
    public SingleOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_singleorder, this);
        initID();
        init();
    }
    
    public void setOrder(Order order){
    	this.tvPrice.setText(String.valueOf(order.price));
    	this.tvAmount.setText(String.valueOf(order.amount));
    	this.tvSum.setText(String.valueOf(order.sum));
 
    	if(this.order == null || this.order.type != order.type){
    		this.setColor(order.type);
    	}
    	this.order = order;
    }
    public void setColor(int type){
    	int corlor = type == Order.BUY ? Color.GREEN : Color.RED;
    	this.tvAmount.setTextColor(corlor);
    	this.tvPrice.setTextColor(corlor);
    	this.tvSum.setTextColor(corlor);
    }

	private void initID()
	{
		this.tvPrice = (TextView)findViewById(R.id.tv_singleorder_price);
		this.tvAmount = (TextView)findViewById(R.id.tv_singleorder_amount);
		this.tvSum = (TextView)findViewById(R.id.tv_singleorder_sum);
	
	}
	private void init()
	{

	}
}
