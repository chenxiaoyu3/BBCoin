package com.chenxiaoyu.bbcoin.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Coin;

public class PriceListFragment extends SherlockFragment{
	
	public static final String TAG = "PriceListFragment";
	ListView mListView;
	Context mContext;
	View mView;
	PriceListViewAdatper mPriceListViewAdatper;
	float mWeight;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	public ListView getPriceListView(){
		return mListView;
	}
	@Override
	public void onAttach(Activity activity) {
		Log.v(TAG, "onAttach");
		mContext = activity;
		mListView.setOnItemClickListener((OnItemClickListener)activity );
		super.onAttach(activity);
	}
	public void setFragmentViewWeight(float weight){
		this.mWeight = weight;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.layout_pricelist, container, false);
		view.setBackgroundColor(Color.RED);
		if (mWeight != 0) {
			LayoutParams lp = new LinearLayout.LayoutParams(0,
	                LayoutParams.MATCH_PARENT, mWeight);
			view.setLayoutParams(lp);
		}
		
		
		mListView = (ListView)view.findViewById(R.id.lv_pricelist);
		mPriceListViewAdatper = new PriceListViewAdatper();
		mListView.setAdapter(mPriceListViewAdatper);
		
		return view;
	}
	
	class PriceListViewAdatper extends BaseAdapter{

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
			SingleCoinView view = new SingleCoinView(mContext);
			view.setCoinID(arg0);
			return view;
		}
		
	}
	

}
