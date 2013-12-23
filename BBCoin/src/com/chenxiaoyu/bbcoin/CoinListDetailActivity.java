package com.chenxiaoyu.bbcoin;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.widget.CStatusDialog;
import com.chenxiaoyu.bbcoin.widget.PriceListFragment;
import com.chenxiaoyu.bbcoin.widget.SingleCoinView;



public class CoinListDetailActivity extends SherlockFragmentActivity implements OnItemClickListener{

	public final String TAG = "CoinListDetailActivity";
	//------- menu
	//------end menu
	PriceListFragment mPriceListFragment;
	CoinStatusFragment mCurRightFragment;
	Handler mHandler;
	Timer mTimer;
	FetchAllTradeListTask mFetchAllTradeListTask;
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

    }
    void init(){
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPriceListFragment = new PriceListFragment();
        mPriceListFragment.setFragmentViewWeight(3);
        
        mCurRightFragment = new CoinStatusFragment();
        mCurRightFragment.setViewWeight(7);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_coinListDetai_root, mPriceListFragment);
        ft.add(R.id.v_coinListDetai_root, mCurRightFragment);
        ft.commit();
        
        mHandler = new Handler(){
        	public void handleMessage(android.os.Message msg) {
        		switch (msg.what) {
				case 0:
					doRefreshRight();
					break;
				default:
					break;
				}
        		
        	};
        };
        mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				doRefreshRight();
			}
		}, 1000);
        
    }
    
    //coin selected 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mCurRightFragment.setCoinID(arg2-1);
		mHandler.sendEmptyMessage(0);
	}
    
	private void doRefreshRight(){
		if (mFetchAllTradeListTask == null) {
			mFetchAllTradeListTask = new FetchAllTradeListTask();
			mFetchAllTradeListTask.execute(0);
		}
	}

	class FetchAllTradeListTask extends AsyncTask<Object, Object, Object>
    {

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		CStatusDialog.Shared.show(CoinListDetailActivity.this);
    	}
		@Override
		protected Object doInBackground(Object... arg0) {
			return Commu.getInstance().fetchAllTradeList();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				DataCenter.getInstance().updateTradeList((List<CoinStatus>)result);
				mCurRightFragment.doRefresh();
			}
			CStatusDialog.Shared.hide();
			mFetchAllTradeListTask = null;
			super.onPostExecute(result);
		}
    }
    

    //------------------------------- Adapter ---------------------------------------
    
    
   //--------------------------------------------Task---------------------------------------

}
