package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.List;

import com.chenxiaoyu.bbcoin.CoinStatus;
import com.chenxiaoyu.bbcoin.Order;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.http.Commu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class CoinStatusView extends LinearLayout{

	View viewOrders;
	ListView listViewOrdersBuy, listViewOrdersSell;
	Context context;
	OrdersListViewAdapter buyOrdersListViewAdapter, sellOrdersListViewAdapter;
	FetchDataTask fetchDataTask = null;
	CoinStatus coinStatus;
	String coinName;
	
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
//		
//		FetchDataTask task = new FetchDataTask();
//		task.execute(0);
//		
	}
	public String getCoinName() {
		return coinName;
	}
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	
	void setCoinStatus(CoinStatus cs){
		this.coinStatus = cs;
		this.buyOrdersListViewAdapter = new OrdersListViewAdapter(cs.buyOrders);
		this.sellOrdersListViewAdapter = new OrdersListViewAdapter(cs.sellOrders);
		this.listViewOrdersBuy.setAdapter(buyOrdersListViewAdapter);
		this.listViewOrdersSell.setAdapter(sellOrdersListViewAdapter);
		
	}
	
	public void refreshCoinStatus(){
		if(fetchDataTask != null) return;
		Log.d("CoinsView", coinName + " fresh now...");
		fetchDataTask = new FetchDataTask();
		fetchDataTask.execute(0);
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
	
	class FetchDataTask extends AsyncTask<Object, Object, CoinStatus>
	{

		@Override
		protected CoinStatus doInBackground(Object... params) {
			CoinStatus cs = Commu.getInstance().fetchCoinStatus(coinName);
			return cs;
		}
		@Override
		protected void onPostExecute(CoinStatus result) {
			if(result != null){
				CoinStatusView.this.setCoinStatus(result);
			}else {
				Toast.makeText(CoinStatusView.this.context, "Error", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}

}
