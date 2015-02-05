package com.chenxiaoyu.bbcoin.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Order;

public class SingleOrderView extends LinearLayout {

    Order order;
    Context context;
    TextView tvPrice, tvSum;
    //	View mContainerView;
    int mWidth, mHeight;
    final String TAG = "CascadeItemView";

    public SingleOrderView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_singleorder, this);
        initID();
        init();


    }

    public SingleOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_singleorder, this);
        initID();
        init();
    }

    public void setOrder(Order order) {

        this.tvPrice.setText(String.format("%.3f", order.price));
//    	this.tvAmount.setText(String.format("%.4f", order.amount));
        this.tvSum.setText(String.format("%.1f", order.sum));


//    	if(this.order == null || this.order.type != order.type){
//    		this.setColor(order.type);
//    	}
        this.order = order;
    }

    public void setColor(int type) {
        int corlor = type == Order.BUY ? Color.GREEN : Color.RED;
        this.tvPrice.setTextColor(corlor);
        this.tvSum.setTextColor(corlor);
    }

    private void initID() {
        this.tvPrice = (TextView) findViewById(R.id.tv_singleorder_price);
//		this.tvAmount = (TextView)findViewById(R.id.tv_singleorder_amount);
        this.tvSum = (TextView) findViewById(R.id.tv_singleorder_sum);
//		this.mContainerView = findViewById(R.id.v_singleorder_container);
    }

    private void init() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (w != 0 && h != 0) {
            Bitmap bt = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(bt);
            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.semi_gray_1));
            double f = order.sum / 20000.0;
            int ww = (int) (w * (f > 1 ? 1 : f));
            canvas.drawRect(new Rect(0, 4, ww, h - 8), p);
            setBackgroundDrawable(new BitmapDrawable(getResources(), bt));
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
