package com.chenxiaoyu.bbcoin.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.KChartActivity;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.OnDataCenterUpdate;
import com.chenxiaoyu.bbcoin.model.Order;

import java.util.Date;
import java.util.TimerTask;

public class CoinStatusView extends LinearLayout implements OnDataCenterUpdate {

    public final String TAG = "CoinStatusView";
    View viewOrders;
    ListView listViewOrdersBuy, listViewOrdersSell;
    LinearLayout buyOrdersView, sellOrdersView;
    TextView nameTextView;
    PriceTextView priceTextView;
    TextView buySumTextView, sellSumTextView;
    Button kChartButton, buyButton, sellButton;
    Context context;

    Date lastRefreshUITime;
    FetchDataTask mFetchDataTask;
    TimerTask mTimerTask;
    public static int CUR_COINID = 0;
    int coinID;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message arg0) {
            switch (arg0.what) {
                case 0:
                    nameTextView.setText(Coin.sGetStrName(coinID));
                    priceTextView.setText(String.format("%.3f", DataCenter.getInstance().getCoinsPrice().prices.get(coinID).getPrice()));
                    buyOrdersView.removeAllViews();
                    sellOrdersView.removeAllViews();
                    CoinStatus cs = DataCenter.getInstance().getAllCoinStatus().get(coinID);
                    float buySum = 0, sellSum = 0;

                    for (Order order : cs.buyOrders) {
                        SingleOrderView view = new SingleOrderView(context);
                        view.setOrder(order);
                        buyOrdersView.addView(view);
                        buySum += order.sum;
                    }
                    for (Order order : cs.sellOrders) {
                        SingleOrderView view = new SingleOrderView(context);
                        view.setOrder(order);
                        sellOrdersView.addView(view);
                        sellSum += order.sum;
                    }

                    lastRefreshUITime = cs.updateTime;
                    buySumTextView.setText(String.format("%.1f", buySum));
                    sellSumTextView.setText(String.format("%.1f", sellSum));
                    break;
                case 1:
                    if (mFetchDataTask == null) {
                        mFetchDataTask = new FetchDataTask();
                        mFetchDataTask.execute(0);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(arg0);
        }
    };

    public CoinStatusView(Context context) {
        super(context);
        this.context = context;
        initID();
        init();
        Log.v(TAG, "Finish create");
    }

    public CoinStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initID();
        init();
    }

    private void initID() {
        LayoutInflater.from(context).inflate(R.layout.layout_coinstatus, this);
        this.viewOrders = findViewById(R.id.v_coinstatus_orders);
        this.buyOrdersView = (LinearLayout) findViewById(R.id.v_orders_buy);
        this.sellOrdersView = (LinearLayout) findViewById(R.id.v_orders_sell);
        this.nameTextView = (TextView) findViewById(R.id.tv_coinstatus_name);
        this.priceTextView = (PriceTextView) findViewById(R.id.tv_coinstatus_curPrice);
        this.kChartButton = (Button) findViewById(R.id.bt_coinstatus_chart);
        this.buySumTextView = (TextView) findViewById(R.id.tv_orders_buySum);
        this.sellSumTextView = (TextView) findViewById(R.id.tv_orders_sellSum);
        this.buyButton = (Button) findViewById(R.id.bt_coinstatus_buy);
        this.sellButton = (Button) findViewById(R.id.bt_coinstatus_sell);
    }

    private void init() {
        this.kChartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, KChartActivity.class);
                intent.putExtra("coinID", coinID);
                context.startActivity(intent);

            }
        });
        this.priceTextView.setMaxFractionNum(3);
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(1);

            }
        };
        this.buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new AlertDialog.Builder(context)
                        .setMessage(R.string.msg_loginFirst)
                        .setPositiveButton(R.string.confirm, null)
                        .show();

            }
        });
        this.sellButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new AlertDialog.Builder(context)
                        .setMessage(R.string.msg_loginFirst)
                        .setPositiveButton(R.string.confirm, null)
                        .show();
            }
        });
    }

    public int getCoinID() {
        return coinID;
    }

    public void setCoinID(int id) {
        this.coinID = id;
        doRefresh();
    }

    public void doRefresh() {

        Message msg = new Message();
        msg.what = 0;
        handler.sendMessageDelayed(msg, 100);
    }

    @Override
    public void onPriceUpdate() {

    }

    @Override
    public void onTradeListUpdate() {
        doRefresh();
    }

    @Override
    protected void onAttachedToWindow() {
//		TimerManager.Instance.addTask(mTimerTask);
        CUR_COINID = coinID;
        if (DataCenter.getInstance().getAllCoinStatus().get(coinID).buyOrders.size() == 0) {
            mTimerTask.run();
        }
        DataCenter.getInstance().addListener(this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
//		TimerManager.Instance.removeTask(mTimerTask);
        DataCenter.getInstance().removeListener(this);
        super.onDetachedFromWindow();
    }

    class FetchDataTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... arg0) {
            CoinStatus cs = Commu.getInstance().fetchSingleTradeList(coinID);
            if (cs != null) {
                DataCenter.getInstance().updateSingleTrade(cs);
            }
            return cs;
        }

        @Override
        protected void onPostExecute(Object result) {
            doRefresh();
            mFetchDataTask = null;
            super.onPostExecute(result);
        }
    }

}
