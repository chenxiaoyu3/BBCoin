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
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.chenxiaoyu.bbcoin.AlarmSetActivity;
import com.chenxiaoyu.bbcoin.BBCoinApp;
import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.MainActivity;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.TimerManager;
import com.chenxiaoyu.bbcoin.Utils;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class PriceListView extends LinearLayout{
	
	public static final String TAG = "PriceListFragment";

	PullToRefreshScrollView mPullToRefreshScrollView;
	LinearLayout mPriceListContainer;
	OnItemClickListener mOnItemClickListener;
	ILoadingLayout mPriceListLoadingLayout;
	Context mContext;
	View mView;

	float mWeight;
	
	TextView mFreshTimeTextView;
	
	FetchDataTask mFetchDataTask;
	TimerTask mUpdateTask;
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
		mPullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.lv_pricelist);
		mPriceListContainer = (LinearLayout)findViewById(R.id.v_priceListContainer);
		mPriceListLoadingLayout = mPullToRefreshScrollView.getLoadingLayoutProxy();

	}
	private void init(){
		
		mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				
				if(mFetchDataTask == null){
					mFetchDataTask = new FetchDataTask();
					mFetchDataTask.execute(0);
				}
			}
		});
		mUpdateTask = new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		};

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		for(int i = 0;i < Coin.COINS.length; i++){
			SingleCoinView view = new SingleCoinView(mContext);
			view.setCoinID(i);
			mPriceListContainer.addView(view, lp);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mOnItemClickListener.onItemClick(null, arg0, 0, 0);
					
				}
			});
			view.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					Intent i = new Intent(mContext, AlarmSetActivity.class);
					i.putExtra("COIN_ID", ((SingleCoinView)v).getCoinID());
					mContext.startActivity(i);
					return false;
				}
			});
		}
		
	}
	
	@Override
	protected void onAttachedToWindow() {
		TimerManager.Instance.addTask(mUpdateTask);
		super.onAttachedToWindow();
	}
	@Override
	protected void onDetachedFromWindow() {
		TimerManager.Instance.removeTask(mUpdateTask);
		super.onDetachedFromWindow();
	}
	public void setOnListItemClickListener(OnItemClickListener listener){
		mOnItemClickListener = listener;
	}
	
	
	public void doRefresh(){
//		mPriceListViewAdatper.doRefresh();
	}
//	class PriceListViewAdatper extends BaseAdapter{
//		
//		List<SingleCoinView> list = new ArrayList<SingleCoinView>();
//		PriceListViewAdatper(){
//			for(int i = 0;i < Coin.COINS.length; i++){
//				SingleCoinView view = new SingleCoinView(mContext);
//				view.setCoinID(i);
//				list.add(view);
//			}
//		}
//		@Override
//		public int getCount() {
//			return Coin.COINS.length;
//			
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public View getView(int arg0, View arg1, ViewGroup arg2) {
//			return list.get(arg0);
//		}
//		
//		public void doRefresh(){
//			for (SingleCoinView view : list) {
//				view.doRefresh();
//			}
//		}
//	}
	
	
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
    		mPullToRefreshScrollView.setRefreshing();
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			CoinsPrice cp = Commu.getInstance().fetchCoinsBuyPrice();
			if (cp != null) {
				DataCenter.getInstance().updateCoinsPrice(cp);
			}
			
//			CoinStatus cs = Commu.getInstance().fetchSingleTradeList(CoinStatusView.CUR_COINID);
//			if (cs != null) {
//				DataCenter.getInstance().updateSingleTrade(cs);
//			}
			return cp ;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			mPullToRefreshScrollView.onRefreshComplete();
			doRefresh();
			mFetchDataTask = null;
			mLastUpdateTime = new Date();
			super.onPostExecute(result);
		}
    }
	

}
