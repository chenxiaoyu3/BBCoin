package com.chenxiaoyu.bbcoin.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class CoinTableView extends LinearLayout{

	Context mContext;
	TableLayout mTableView; 
	PullToRefreshScrollView mPullToRefreshScrollView;
	FetchDataTask mFetchDataTask;
	List<SingleCoinBlockView> mSingleCoinBlockViews;
	Handler mHandler;
	Timer mTimer;
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

	private void initID(){
		mTableView = (TableLayout)findViewById(R.id.v_coinTable);
		mPullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.v_coinTable_scroll);
	}
	private void init(){
		mSingleCoinBlockViews = new ArrayList<SingleCoinBlockView>();
		TableRow row = null;
		int i = 0;
		for(; i < Coin.COINS.length; i++){
			if (i % 3 == 0) {
				if (row != null) {
					mTableView.addView(row);
				}
				row = new TableRow(mContext);
			}
			SingleCoinBlockView view = new SingleCoinBlockView(mContext);
			view.setCoinID(i);
			row.addView(view);
			mSingleCoinBlockViews.add(view);
		}
		if (i % 3 != 0) {
			mTableView.addView(row);
		}
		
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
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
			}
		}, 10*1000, 30*1000);
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
			mFetchDataTask = null;
		}
    }
	
}
