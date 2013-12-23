package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.MainActivity;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.Utils;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.Order;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CoinStatusView extends LinearLayout{

	public final String TAG = "CoinStatusView";
	View viewOrders;
	ListView listViewOrdersBuy, listViewOrdersSell;
	LinearLayout buyOrdersView, sellOrdersView;
	TextView nameTextView, priceTextView;
	Context context;

	Date lastRefreshUITime;
	int coinID;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message arg0) {
			switch (arg0.what) {
			case 0:
				nameTextView.setText(Coin.sGetStrName(coinID));
				priceTextView.setText(String.format("%.3f", DataCenter.getInstance().getCoinsPrice().prices.get(coinID).getPrice()));
				buyOrdersView.removeAllViews();
				sellOrdersView.removeAllViews();
				CoinStatus cs = (CoinStatus)arg0.obj;
				for (Order order : cs.buyOrders) {
					SingleOrderView view = new SingleOrderView(context);
					view.setOrder(order);
					buyOrdersView.addView(view);
				}
				for (Order order : cs.sellOrders) {
					SingleOrderView view = new SingleOrderView(context);
					view.setOrder(order);
					sellOrdersView.addView(view);
				}
				
				lastRefreshUITime = cs.updateTime;
				break;

			default:
				break;
			}
			super.handleMessage(arg0);
		}
	};
	public CoinStatusView(Context context) {
		super(context);
		this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_coinstatus, this);
        initID();
        init();
	}
	
	private void initID()
	{
		this.viewOrders = findViewById(R.id.v_coinstatus_orders);

		this.buyOrdersView = (LinearLayout)findViewById(R.id.v_orders_buy);
		this.sellOrdersView = (LinearLayout)findViewById(R.id.v_orders_sell);
		this.nameTextView = (TextView)findViewById(R.id.tv_coinstatus_name);
		this.priceTextView = (TextView)findViewById(R.id.tv_coinstatus_curPrice);
	}
	private void init()
	{
//		this.buyOrdersListViewAdapter = new OrdersListViewAdapter(cs.buyOrders);
//		this.sellOrdersListViewAdapter = new OrdersListViewAdapter(cs.sellOrders);
//		this.listViewOrdersBuy.setAdapter(buyOrdersListViewAdapter);
//		this.listViewOrdersSell.setAdapter(sellOrdersListViewAdapter);
	}

	public int getCoinID() {
		return coinID;
	}
	public void setCoinID(int id) {
		this.coinID = id;
	}
	
	public void doRefresh(){
//		Log.d(TAG, coinID + " refresh");
		CoinStatus cs = DataCenter.getInstance().getAllCoinStatus().get(coinID);
		if (this.lastRefreshUITime  != null && Utils.timeCompareTo(cs.updateTime, this.lastRefreshUITime) <= 0) {
			Log.d(TAG,"No need");
			return;
		}
		Message msg = new Message();
		msg.what = 0;
		msg.obj = cs;
		handler.sendMessageDelayed(msg, 200);
		
	}
	
//	class OrdersListViewAdapter extends BaseAdapter{
//		
//		List<Order> orders;
//		@Override
//		public int getCount() {
//			return orders.size();
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			return orders.get(arg0);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public View getView(int arg0, View arg1, ViewGroup arg2) {
//			View retView = null;
//			if (arg1 == null) {
//				retView = new SingleOrderView(context);
//				((SingleOrderView)retView).setOrder(orders.get(arg0));
//			}else{
//				retView = arg1;
//				((SingleOrderView)retView).setOrder(orders.get(arg0));
//			}
//			return retView;
//		}
//		
//		
//		public OrdersListViewAdapter(List<Order> orders) {
//			this.orders = orders;
//			
//		}
//		public void setOrders(List<Order> orders){
//			this.orders = orders;
//		}
//		
//	}
	
//	class FetchDataTask extends AsyncTask<Object, Object, CoinStatus>
//	{
//
//		@Override
//		protected CoinStatus doInBackground(Object... params) {
//			CoinStatus cs = Commu.getInstance().fetchCoinStatus(coinName);
//			return cs;
//		}
//		@Override
//		protected void onPostExecute(CoinStatus result) {
//			if(result != null){
//				CoinStatusView.this.setCoinStatus(result);
//			}else {
//				Toast.makeText(CoinStatusView.this.context, "Error", Toast.LENGTH_SHORT).show();
//			}
//			super.onPostExecute(result);
////		}
//		
//	}

}
