package com.chenxiaoyu.bbcoin;

import com.chenxiaoyu.bbcoin.widget.CoinStatusView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class CoinStatusFragment extends Fragment {
	
	String coinName;
	CoinStatusView coinStatusView;
	public CoinStatusFragment()
	{
		super();
	}
	public CoinStatusFragment(String coinName) {
		super();
		this.coinName = coinName;
		
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

    	coinStatusView = new CoinStatusView(getActivity().getApplicationContext());
    	coinStatusView.setCoinName(this.coinName);
        return coinStatusView;
    }
    
    public void refreshCoinStatus(){
    	if(this.coinStatusView != null){
    		this.coinStatusView.refreshCoinStatus();
    	}
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(KEY_CONTENT, mContent);
//    }
    
}
