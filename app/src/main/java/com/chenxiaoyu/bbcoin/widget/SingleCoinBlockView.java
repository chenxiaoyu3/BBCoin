package com.chenxiaoyu.bbcoin.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenxiaoyu.bbcoin.DataCenter;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Coin;

public class SingleCoinBlockView extends LinearLayout {

    Context mContext;
    TextView mNameTextView, mPriceTextView, mPriceTextView2;
    int mCoinID;
    double mLastPrice;

    public SingleCoinBlockView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_singlecoinblock, this);
        this.mContext = context;
        initID();
    }

    private void initID() {
        mNameTextView = (TextView) findViewById(R.id.tv_singleCoinBlock_name);
        mPriceTextView = (TextView) findViewById(R.id.tv_singleCoinBlock_price);
        mPriceTextView2 = (TextView) findViewById(R.id.tv_singleCoinBlock_price2);
    }

    public void setCoinID(int id) {
        this.mCoinID = id;
        mNameTextView.setText(Coin.getStrNameWithChs(id));
    }

    public void doRefresh() {
        double price = DataCenter.getInstance().getCoinsPrice().prices.get(mCoinID).getPrice();
        int fractionNum = 3;
        if (price > 100) {
            fractionNum = 2;
        }
        if (price > 1000) {
            fractionNum = 1;
        }
        mPriceTextView.setText(String.format("%." + fractionNum + "f", price));
        if (mLastPrice != 0) {
            if (price > mLastPrice) {

                mPriceTextView2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.triangle_up, 0, 0, 0);
                mPriceTextView2.setTextColor(getResources().getColor(R.color.green));
                mPriceTextView.setTextColor(getResources().getColor(R.color.green));
            } else if (price < mLastPrice) {
                mPriceTextView2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.triangle_down, 0, 0, 0);
                mPriceTextView.setTextColor(getResources().getColor(R.color.red));
                mPriceTextView2.setTextColor(getResources().getColor(R.color.red));
            } else {
                mPriceTextView2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.triangle_m, 0, 0, 0);
                mPriceTextView.setTextColor(getResources().getColor(R.color.white));
                mPriceTextView2.setTextColor(getResources().getColor(R.color.white));
            }
            mPriceTextView2.setText(String.format("%.3f", Math.abs(price - mLastPrice)));
        }
        mLastPrice = price;
    }

}
