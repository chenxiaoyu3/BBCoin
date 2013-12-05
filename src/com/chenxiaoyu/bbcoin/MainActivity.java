package com.chenxiaoyu.bbcoin;


import com.chenxiaoyu.bbcoin.widget.CoinStatusView;

import android.app.Activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends Activity {

	CoinStatusView coinStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.coinStatusView = (CoinStatusView)findViewById(R.id.v_main_coinStatus);
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			this.coinStatusView.test();
			break;

		default:
			break;
		}
    	return super.onTouchEvent(event);
    }

}
