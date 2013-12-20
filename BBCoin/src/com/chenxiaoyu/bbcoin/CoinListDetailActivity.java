package com.chenxiaoyu.bbcoin;

import java.util.List;

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
import com.chenxiaoyu.bbcoin.widget.PriceListFragment;



public class CoinListDetailActivity extends SherlockFragmentActivity implements OnItemClickListener{

	public final String TAG = "CoinListDetailActivity";
	//------- menu
	//------end menu
	PriceListFragment mPriceListFragment;
	CoinStatusFragment mCurRightFragment;
	Handler mHandler;
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
        		new FetchAllTradeListTask().execute(0);
        	};
        };
    }
    
    //coin selected 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mCurRightFragment.setCoinID(arg2);
		mCurRightFragment.doRefresh();
		mHandler.sendEmptyMessage(0);
	}
    
	
	class FetchAllTradeListTask extends AsyncTask<Object, Object, Object>
    {

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
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
			
			super.onPostExecute(result);
		}
    	
    }
    

    //------------------------------- Adapter ---------------------------------------
    
    
   //--------------------------------------------Task---------------------------------------
   
    

}
