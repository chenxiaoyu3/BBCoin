package com.chenxiaoyu.bbcoin.widget;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.TimerTask;


public class TradeMarketView extends LinearLayout implements OnItemClickListener {

    public final String TAG = "TradeMarketView";
    //------- menu

    //------end menu
    PriceListView mPriceListView;
    CoinStatusView mCoinStatusView;
    Handler mHandler;
    //	Timer mTimer;
    TimerTask mRefreshTask;
    LinearLayout mRootView;
    ScrollView mRightScrollView;
    LinkedList<SoftReference<CoinStatusView>> mCoinStatusViewList;
    Context mContext;

    public TradeMarketView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_trademarket, this);
        mContext = context;
        initIDs();
        init();
    }

    void initIDs() {
        mRootView = (LinearLayout) findViewById(R.id.v_coinListDetai_root);
        mRightScrollView = (ScrollView) findViewById(R.id.sv_coinListDetail);
        mPriceListView = (PriceListView) findViewById(R.id.v_coinListDetai_Left);
        mCoinStatusView = (CoinStatusView) findViewById(R.id.v_coinListDetai_Right);
    }

    void init() {

        DataCenter.getInstance().addListener(mCoinStatusView);

        mCoinStatusViewList = new LinkedList<SoftReference<CoinStatusView>>();
        for (int i = 0; i < Coin.COINS.length; i++) {
//        	CoinStatusView csv = new CoinStatusView(mContext);
//        	csv.setCoinID(i);
            mCoinStatusViewList.add(new SoftReference<CoinStatusView>(null));
        }

        mPriceListView.setOnListItemClickListener(this);
        mCoinStatusView.doRefresh();
    }


    //coin selected 
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        SingleCoinView v = (SingleCoinView) arg1;
        coinViewSwith(v.getCoinID());
    }

    public void coinViewSwith(int coinID) {
        if (mCoinStatusView.getCoinID() == coinID) {
            return;
        }
        mRightScrollView.removeView(mCoinStatusView);
        SoftReference<CoinStatusView> vv = mCoinStatusViewList.get(coinID);

        if (vv.get() == null) {
            CoinStatusView csv = new CoinStatusView(mContext);
            csv.setCoinID(coinID);
            vv = new SoftReference<CoinStatusView>(csv);

            mCoinStatusViewList.set(coinID, vv);
            mCoinStatusView = csv;
        } else {
            mCoinStatusView = vv.get();
        }
        mRightScrollView.addView(mCoinStatusView);
        mCoinStatusView.doRefresh();
    }

    @Override
    protected void onAttachedToWindow() {
        MobclickAgent.onEventBegin(mContext, "ViewTradeMarket");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        MobclickAgent.onEventEnd(mContext, "ViewTradeMarket");
        super.onDetachedFromWindow();
    }


    //------------------------------- Adapter ---------------------------------------


    //--------------------------------------------Task---------------------------------------

}
