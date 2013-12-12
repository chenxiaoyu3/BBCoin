package com.chenxiaoyu.bbcoin;

import com.chenxiaoyu.bbcoin.widget.CoinStatusView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public final class CoinStatusFragment extends Fragment {
	
	public final String TAG = "CoinStatusFragment";
	int coinID;
	public int getCoinID() {
		return coinID;
	}
	public void setCoinID(int coinID) {
		this.coinID = coinID;
	}

	CoinStatusView coinStatusView;
	Context context;
	public CoinStatusFragment()
	{
		super();
	}
	public CoinStatusFragment(int coinID) {
		super();
		this.coinID = coinID;
	}
	
	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}
	@Override
	public void onStart() {
		Log.v(TAG, "fragment start  " + coinID);
		if(coinStatusView != null){
			coinStatusView.doRefresh();
		}
		super.onStart();
	}
	@Override
	public void onResume() {
		Log.v(TAG, "fragment resume  " + coinID);
		super.onResume();
	}
	@Override
	public void onDestroyView() {
		Log.v(TAG, "View destroy  " + coinID);
		super.onDestroyView();
	}

//    private String mContent = "???";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//            mContent = savedInstanceState.getString(KEY_CONTENT);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	coinStatusView = new CoinStatusView(this.context);
    	coinStatusView.setCoinID(this.coinID);
    	Log.d(TAG, "View create  " + coinID);
        return coinStatusView;
    }
    
    public void doRefresh(){
    	if(coinStatusView != null){
			coinStatusView.doRefresh();
		}
    }
    
//    public void refreshCoinStatus(){
//    	if(this.coinStatusView != null){
//    		this.coinStatusView.refreshCoinStatus();
//    	}
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(KEY_CONTENT, mContent);
//    }
    
}
