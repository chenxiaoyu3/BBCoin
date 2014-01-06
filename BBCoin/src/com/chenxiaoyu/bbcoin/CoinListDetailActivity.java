package com.chenxiaoyu.bbcoin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.widget.CStatusDialog;
import com.chenxiaoyu.bbcoin.widget.CoinStatusView;
import com.chenxiaoyu.bbcoin.widget.PriceListFragment;
import com.chenxiaoyu.bbcoin.widget.PriceListView;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;



public class CoinListDetailActivity extends SherlockActivity implements OnItemClickListener{

	public final String TAG = "CoinListDetailActivity";
	//------- menu
	
	//------end menu
	PriceListView mPriceListView;
	CoinStatusView mCoinStatusView;
	Handler mHandler;
	Timer mTimer;
	LinearLayout mRootView;
	ScrollView mRightScrollView;
	List<CoinStatusView> mCoinStatusViewList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinlist_detail);

        initIDs();       
        init();
    }
    
    @Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
    void initIDs(){
    	mRootView = (LinearLayout)findViewById(R.id.v_coinListDetai_root);
    	mRightScrollView = (ScrollView)findViewById(R.id.sv_coinListDetail);
    	mPriceListView = (PriceListView)findViewById(R.id.v_coinListDetai_Left);
    	mCoinStatusView = (CoinStatusView)findViewById(R.id.v_coinListDetai_Right);
    }
    void init(){
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DataCenter.getInstance().addListener(mCoinStatusView);
        
        mCoinStatusViewList = new ArrayList<CoinStatusView>();
        for(int i = 0;i < Coin.COINS.length; i++){
        	CoinStatusView csv = new CoinStatusView(this);
        	csv.setCoinID(i);
        	mCoinStatusViewList.add(csv);
        }
        
        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_list));
        getSupportActionBar().setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.seperator_ver));
        getSupportActionBar().setCustomView(R.layout.layout_actionbar_title);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbarTitle)).setText(getString(R.string.app_name));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
   
    }
    
    
    
    //coin selected 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		SingleCoinView v = (SingleCoinView)arg1;
		mRightScrollView.removeView(mCoinStatusView);
		mCoinStatusView = mCoinStatusViewList.get(v.getCoinID());
		mRightScrollView.addView(mCoinStatusView);
		mCoinStatusView.doRefresh();
	}
    

	
    

    //------------------------------- Adapter ---------------------------------------
    
    
   //--------------------------------------------Task---------------------------------------

}
