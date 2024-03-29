package com.chenxiaoyu.bbcoin.widget;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Coin;

public class SingleCoinView extends LinearLayout {

    TextView nameTextView, priceTextView;
    Button mButton;
    final String TAG = "SingleCoinView";
    int coinID;
    double mLastPrice;
    Context context;

    public SingleCoinView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_singlecoin, this);
        initID();
        init();

    }

    private void initID() {
        this.priceTextView = (TextView) findViewById(R.id.tv_singlecoin_price);
        this.nameTextView = (TextView) findViewById(R.id.tv_singlecoin_name);

    }

    private void init() {
//		this.mButton.setOnLongClickListener(new View.OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View arg0) {
//				Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//				Intent i = new Intent(context, AlarmSetActivity.class);
//				i.putExtra("COIN_ID", coinID);
//				context.startActivity(i);
//				return false;
//			}
//		});
    }

    void setColor(int color) {
        this.priceTextView.setTextColor(color);
        this.nameTextView.setTextColor(color);
    }

    public void setCoinID(int id) {
        this.coinID = id;
        this.nameTextView.setText(Coin.sGetStrName(id));
        doRefresh();
    }

    public int getCoinID() {
        return this.coinID;
    }

    public void doRefresh() {
        Coin c = DataCenter.getInstance().getCoinsPrice().prices.get(coinID);
        double newPrice = c.getPrice();

        this.priceTextView.setText(String.format("%.3f", newPrice));
        if (mLastPrice != 0 && newPrice != 0) {

            if (newPrice < mLastPrice) {
                this.setColor(Color.RED);
            } else if (newPrice > mLastPrice) {
                this.setColor(Color.GREEN);
            } else {
                this.setColor(Color.WHITE);
            }
        }
        mLastPrice = newPrice;
    }
}
