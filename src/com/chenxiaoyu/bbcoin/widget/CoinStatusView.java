package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.List;

import com.chenxiaoyu.bbcoin.Order;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.http.Commu;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CoinStatusView extends LinearLayout{

	View viewOrders;
	ListView listViewOrdersBuy, listViewOrdersSell;
	Context context;
	OrdersListViewAdapter buyOrdersListViewAdapter, sellOrdersListViewAdapter;
	public CoinStatusView(Context context) {
		super(context);
		this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_coinstatus, this);
        initID();
        init();
	}
	public CoinStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_coinstatus, this);
        initID();
        init();
    }
	
	private void initID()
	{
		this.viewOrders = findViewById(R.id.v_coinstatus_orders);
		this.listViewOrdersBuy = (ListView)findViewById(R.id.lv_orders_buy);
		this.listViewOrdersSell = (ListView)findViewById(R.id.lv_orders_sell);
	
	}
	private void init()
	{
		
	}
	
	public void test()
	{
		Order o = new Order();
		o.price = 1343.23423;
		o.amount = 234;
		o.sum = 23423.23535;
		List<Order> orders = new ArrayList<Order>();
		for(int i = 0; i < 10; i++)
			orders.add(o);
		buyOrdersListViewAdapter = new OrdersListViewAdapter(orders);
		
		this.listViewOrdersBuy.setAdapter(buyOrdersListViewAdapter);
		this.listViewOrdersSell.setAdapter(buyOrdersListViewAdapter);
		
	}
	
	class OrdersListViewAdapter extends BaseAdapter{
		
		List<Order> orders;
		int type;
		@Override
		public int getCount() {
			return orders.size();
		}

		@Override
		public Object getItem(int arg0) {
			return orders.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View retView = null;
			if (arg1 == null) {
				retView = new SingleOrderView(context);
				((SingleOrderView)retView).setOrder(orders.get(arg0));
			}else{
				retView = arg1;
				((SingleOrderView)retView).setOrder(orders.get(arg0));
			}
			return retView;
		}
		
		
		public OrdersListViewAdapter(List<Order> orders) {
			this.orders = orders;
			
		}
		public void setOrders(List<Order> orders){
			this.orders = orders;
		}
		
	}
	
	class FetchDataTask extends AsyncTask<Object, Object, Object>
	{

		@Override
		protected Object doInBackground(Object... params) {
			String retString = Commu.getInstance().sendHttpClientPost("http://ggcoin.sinaapp.com/fetchData/tradeList38", null, "utf-8");
			if(retString != null){
				
			}
			return null;
		}
		
	}

}
