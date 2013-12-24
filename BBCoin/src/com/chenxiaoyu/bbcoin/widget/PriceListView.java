package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.chenxiaoyu.bbcoin.AlarmSetActivity;
import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.MainActivity;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.Utils;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PriceListView extends LinearLayout{
	
	public static final String TAG = "PriceListFragment";
	PullToRefreshListView mListView;
	ILoadingLayout mPriceListLoadingLayout;
	Context mContext;
	View mView;
	PriceListViewAdatper mPriceListViewAdatper;
	float mWeight;
	
//	ProgressBar mProgressBar;
	TextView mFreshTimeTextView;
	
	FetchDataTask mFetchDataTask;
	Timer mTimer;
	Date mLastUpdateTime;
	Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case 0:
				if(mFetchDataTask == null){
					mFetchDataTask = new FetchDataTask();
					mFetchDataTask.execute(0);
				}
				break;
			case 1:
				if (mLastUpdateTime != null && mContext != null && mFetchDataTask == null) {
					mPriceListLoadingLayout.setLastUpdatedLabel(Utils.timePassed(mContext, mLastUpdateTime) + mContext.getString(R.string.passed));
				}
				break;
			default:
				break;
			}
    		
    	};
    };
	public PriceListView(Context context) {
		super(context);
		mContext = context;
		initID();
		init();
	}
	public PriceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initID();
		init();
	}
	
	private void initID(){
		LayoutInflater.from(mContext).inflate(R.layout.layout_pricelist, this);
		mListView = (PullToRefreshListView)findViewById(R.id.lv_pricelist);
		mPriceListLoadingLayout = mListView.getLoadingLayoutProxy();
//		mProgressBar = (ProgressBar)findViewById(R.id.pb_coinListDetai_loading);
//		mFreshTimeTextView = (TextView)findViewById(R.id.tv_coinListDetail_refresh);
	}
	private void init(){
		mPriceListViewAdatper = new PriceListViewAdatper();
		mListView.setAdapter(mPriceListViewAdatper);
		mListView.setOnItemClickListener((OnItemClickListener)mContext);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if(mFetchDataTask == null){
					mFetchDataTask = new FetchDataTask();
					mFetchDataTask.execute(0);
				}
			}
		});
		mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 1000, 1*30*1000);
        mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
			}
		}, 1000, 10*1000);
        mListView.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent i = new Intent(mContext, AlarmSetActivity.class);
				i.putExtra("COIN_ID", ((SingleCoinView)arg1).getCoinID());
				mContext.startActivity(i);
				return false;
			}
		});
	}
	
	
	
	public void doRefresh(){
		mPriceListViewAdatper.doRefresh();
	}
	class PriceListViewAdatper extends BaseAdapter{
		
		List<SingleCoinView> list = new ArrayList<SingleCoinView>();
		PriceListViewAdatper(){
			for(int i = 0;i < Coin.COINS.length; i++){
				SingleCoinView view = new SingleCoinView(mContext);
				view.setCoinID(i);
				list.add(view);
			}
		}
		@Override
		public int getCount() {
			return Coin.COINS.length;
			
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return list.get(arg0);
		}
		
		public void doRefresh(){
			for (SingleCoinView view : list) {
				view.doRefresh();
			}
		}
	}
	
	
//	class FetchPricesTask extends AsyncTask<Object, Object, Object>{
//
//    	@Override
//    	protected void onPreExecute() {
////    		mProgressBar.setVisibility(View.VISIBLE);
////    		mFreshTimeTextView.setText("Refresh...");
//    		mListView.setRefreshing();
//    		super.onPreExecute();
//    	}
//		@Override
//		protected Object doInBackground(Object... arg0) {
//			return Commu.getInstance().fetchCoinsBuyPrice();
//		}
//		
//		@Override
//		protected void onPostExecute(Object arg0) {
//			DataCenter.getInstance().updateCoinsPrice((CoinsPrice)arg0);
//			doRefresh();
////			mProgressBar.setVisibility(View.INVISIBLE);
//			mFetchPricesTask = null;
//			mLastUpdateTime = new Date();
//			
//			mListView.onRefreshComplete();
//			super.onPostExecute(arg0);
//		}
//    }
	class FetchDataTask extends AsyncTask<Object, Object, Object>
    {

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		mListView.setRefreshing();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			CoinsPrice cp = Commu.getInstance().fetchCoinsBuyPrice();
			if (cp != null) {
				DataCenter.getInstance().updateCoinsPrice(cp);
			}
			
			List<CoinStatus> cs = Commu.getInstance().fetchAllTradeList();
			if (cs != null) {
				DataCenter.getInstance().updateTradeList(cs);
			}
			return cp != null && cs != null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			mListView.onRefreshComplete();
			doRefresh();
			mFetchDataTask = null;
			mLastUpdateTime = new Date();
			super.onPostExecute(result);
		}
    }
	

}
