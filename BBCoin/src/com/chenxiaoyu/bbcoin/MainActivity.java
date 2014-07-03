package com.chenxiaoyu.bbcoin;



import java.io.ByteArrayOutputStream;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.actionbarsherlock.widget.ShareActionProvider.OnShareTargetSelectedListener;
import com.chenxiaoyu.bbcoin.widget.CoinTableView;
import com.chenxiaoyu.bbcoin.widget.SplashView;
import com.chenxiaoyu.bbcoin.widget.TradeMarketView;
import com.chenxiaoyu.bbcoin.widget.SplashView.SplashListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;




import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;



public class MainActivity extends SherlockActivity implements SplashListener{

	public final String TAG = "MainActivity";
	
	final String ShareFileName = "BBCoinShare.png";
	//------- menu
	SlidingMenu mSlidingMenu;
	Button mMenuQQLogon, mMenuSetting, mMenuExit;
	Button mMenuPriceOverview, mMenuTradeMarket;
	//------end menu
	SplashView mSplashView;
	TradeMarketView mTradeMarketView;
	CoinTableView mCoinTableView;
	ViewGroup mRootView;
	int mCurRightView;
	static final int VIEW_TRADE_MARKET = 1, VIEW_COIN_TABLE = 0;
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			getSupportActionBar().show();
		};
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	BBCoinApp.MainActivity = this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	DataCenter.getInstance();
    	getSupportActionBar().setIcon(R.drawable.ic_list);
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().hide();
    	
        Intent i = new Intent(this, SplashActivity.class);
    	startActivityForResult(i, 1);
    	
        setContentView(R.layout.activity_main);
        initIDs();
        init();        
    }
    void initSlidingMenu(){
    	mSlidingMenu = new SlidingMenu(this);
    	mSlidingMenu.setMode(SlidingMenu.LEFT);
    	mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
    	mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    	mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    	mSlidingMenu.setShadowDrawable(R.drawable.shadow);
    	mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    	mSlidingMenu.setMenu(R.layout.layout_sliding_menu);
        
        mMenuQQLogon = (Button)findViewById(R.id.bt_slideMenu_qqlogon);
    	mMenuSetting = (Button)findViewById(R.id.bt_slideMenu_setting);
        mMenuPriceOverview = (Button)findViewById(R.id.bt_slideMenu_priceOverview);
        mMenuTradeMarket = (Button)findViewById(R.id.bt_slideMenu_tradeMarket);
        mMenuExit = (Button)findViewById(R.id.bt_slideMenu_exit);
            
        ((TextView)findViewById(R.id.tv_slideMenu_version)).setText(BBCoinApp.Application.getVersionName());
        mMenuQQLogon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(MainActivity.this, "QQLoginClick");
				new AlertDialog.Builder(MainActivity.this)
				.setMessage(R.string.msg_loginNotFinished)
				.setPositiveButton(R.string.confirm, null)
				.show();
			}
		});
        mMenuSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(i);
			}
		});
        mMenuPriceOverview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpCoinTable();
			}
		});
        mMenuTradeMarket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpTradeMarket();
			}
		});
        mMenuExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BBCoinApp.Application.closeApp();
			}
		});
    }
    @Override
	protected void onDestroy() {

		super.onDestroy();
	}
    void initIDs(){
    	mRootView = (ViewGroup)findViewById(R.id.v_main_root);
    }
    void init(){
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
//    	mSplashView = new SplashView(this);
//    	mSplashView.setSplashListener(this);
    	mCoinTableView = new CoinTableView(this);
    	
//    	mRootView.addView(mSplashView);
//    	mSplashView.playAnimation();
    	
    	
    	
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
	    	case 1:
	    		getSupportActionBar().show();
		    	mRootView.addView(mCoinTableView);
				initSlidingMenu();
				guideStart();
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
	@Override
	public void onSplashFinish() {
		
//		mRootView.removeView(mSplashView);
		mSplashView.setVisibility(View.GONE);
		getSupportActionBar().show();
		mRootView.addView(mCoinTableView);
		initSlidingMenu();
		
	}
    void jumpTradeMarket(){
    	
    	if (mCurRightView == VIEW_TRADE_MARKET) {
    		mSlidingMenu.showContent(true);
		}else{
			mSlidingMenu.showContent(false);
			mRootView.removeAllViews();
	    	mCoinTableView = null;
	    	mTradeMarketView = new TradeMarketView(this);
	    	mRootView.addView(mTradeMarketView);
	    	mCurRightView = VIEW_TRADE_MARKET;
		}
    	
    }
    void jumpCoinTable(){
    	
    	if (mCurRightView == VIEW_COIN_TABLE) {
    		mSlidingMenu.showContent(true);
		}else {
			mSlidingMenu.showContent(false);
			mRootView.removeAllViews();
	    	mTradeMarketView = null;
	    	mCoinTableView = new CoinTableView(this);
	    	mRootView.addView(mCoinTableView);
	    	mCurRightView = VIEW_COIN_TABLE;
		}

    }
    public void onCoinBlockClicked(int coinID){
    	jumpTradeMarket();
    	mTradeMarketView.coinViewSwith(coinID);
    }

    public void onResume() {
    	super.onResume();
    	Log.v(TAG, "onResume");
    	MobclickAgent.onResume(this);
    	TimerManager.Instance.resume();
    }
	public void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
		MobclickAgent.onPause(this);
		TimerManager.Instance.pause();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
		case 0:
			mSlidingMenu.toggle();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	};
	public void setActionBarLoading(boolean isLoading){
		setSupportProgressBarIndeterminateVisibility(isLoading);
	}
	

    //------------------------------- Share ---------------------------------------
	Intent mShareIntent;
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.menu_share, menu);
	
	    MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
	    ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
	    actionProvider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener() {
			
			@Override
			public boolean onShareTargetSelected(ShareActionProvider source,
					Intent intent) {
				saveScreenShot();
				MobclickAgent.onEvent(MainActivity.this, "Share", intent.getAction());
				return false;
			}
		});
	    actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
	    mShareIntent = new Intent(Intent.ACTION_SEND);
	    mShareIntent.setType("image/*");
		Uri uri = Uri.fromFile(Utils.readSDFile(ShareFileName));
		mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
	    actionProvider.setShareIntent(mShareIntent);
	    return true;
	}

	private void saveScreenShot() {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	Utils.getBitmapFromView(mRootView,getString(R.string.share_msg)).compress(Bitmap.CompressFormat.PNG, 100, baos);
        Utils.writeSDFile(baos.toByteArray(), ShareFileName);
	    
	}
	    
	    
    
   //--------------------------------------------Guide---------------------------------------
	void guideStart(){
		if (PreferManager.Instance().get(this, "firstlaunche") == null) {
			final ImageView imageView = new ImageView(this);
			imageView.setImageResource(R.drawable.guide);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mRootView.removeView(imageView);
				}
			});
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			mRootView.addView(imageView, lp);
			PreferManager.Instance().set(this, "firstlaunche", "1");
		}
		
		
	}
    

}
