package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.AlarmSetActivity;
import com.chenxiaoyu.bbcoin.BBCoinApp;
import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.TimerManager;
import com.chenxiaoyu.bbcoin.Utils;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.service.AlarmManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class CoinTableView extends LinearLayout{
	public static final String TAG = "CoinTableView";
	Context mContext;
	TableLayout mTableView; 
	PullToRefreshScrollView mPullToRefreshScrollView;
	FetchDataTask mFetchDataTask;
	List<SingleCoinBlockView> mSingleCoinBlockViews;
	Handler mHandler;
//	public static Timer sTimer;
	TimerTask mRefreshTask;
	public CoinTableView(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_coin_table, this);
		initID();
		init();
	}
	public CoinTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_coin_table, this);
		initID();
		init();
	}
	@Override
	protected void onAttachedToWindow() {
		Log.v(TAG, "attached");
		TimerManager.Instance.addTask(mRefreshTask);
		MobclickAgent.onEventBegin(mContext, "ViewCoinTable");
		super.onAttachedToWindow();
	}
	@Override
	protected void onDetachedFromWindow() {
		Log.v(TAG, "Detached");
		TimerManager.Instance.removeTask(mRefreshTask);
		MobclickAgent.onEventEnd(mContext, "ViewCoinTable");
		super.onDetachedFromWindow();
	}
	private void initID(){
		mTableView = (TableLayout)findViewById(R.id.v_coinTable);
		mPullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.v_coinTable_scroll);
	}
	private void init(){
		mSingleCoinBlockViews = new ArrayList<SingleCoinBlockView>();
		TableRow row = null;
		TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
		int i = 0;
		for(; i < Coin.COINS.length; i++){
			if (i % 4 == 0) {
				if (row != null) {
					mTableView.addView(row);
				}
				row = new TableRow(mContext);
			}
			SingleCoinBlockView view = new SingleCoinBlockView(mContext);
			view.setCoinID(i);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					BBCoinApp.MainActivity.onCoinBlockClicked(  ((SingleCoinBlockView)arg0).mCoinID );
				}
			});
			view.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					Intent i = new Intent(mContext, AlarmSetActivity.class);
					i.putExtra("COIN_ID", ((SingleCoinBlockView)v).mCoinID);
					mContext.startActivity(i);
					return false;
				}
			});
			row.addView(view,lp);
			mSingleCoinBlockViews.add(view);
		}
		mTableView.addView(row);
		
		
		mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (mFetchDataTask == null) {
					mFetchDataTask = new FetchDataTask(true);
					mFetchDataTask.execute(0);
				}
			}
		});
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1://timer
					if (mFetchDataTask == null) {
						mFetchDataTask = new FetchDataTask(false);
						mFetchDataTask.execute(0);
					}
					break;
				case 2://first time
					mFetchDataTask = new FetchDataTask(true);
					mFetchDataTask.execute(0);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		mRefreshTask = new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
			}
		};
		mHandler.sendEmptyMessage(2);
	}
	
	class FetchDataTask extends AsyncTask<Object, Object, Object>
    {
		boolean needScroll;
		public FetchDataTask(boolean needScroll) {
			this.needScroll = needScroll;
		}
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		if (needScroll) {
				mPullToRefreshScrollView.setRefreshing();
			}else{
				BBCoinApp.MainActivity.setActionBarLoading(true);
			}
    		
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			CoinsPrice cp = Commu.getInstance().fetchCoinsBuyPrice();
			if (cp != null) {
				DataCenter.getInstance().updateCoinsPrice(cp);
			}

			return cp;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			
			if (result != null) {
				for(SingleCoinBlockView v : mSingleCoinBlockViews){
					v.doRefresh();
				}
			}
			mPullToRefreshScrollView.onRefreshComplete();
			BBCoinApp.MainActivity.setActionBarLoading(false);
			mFetchDataTask = null;
			mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.timeFormat(new Date(), "HH:mm:ss"));
			AlarmManager.Instance.doCheckAndAlarm(mContext, DataCenter.getInstance().getCoinsPrice());
		}
    }
	
}
