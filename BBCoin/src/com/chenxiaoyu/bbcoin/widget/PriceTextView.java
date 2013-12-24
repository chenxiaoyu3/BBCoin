package com.chenxiaoyu.bbcoin.widget;

import java.util.Locale;

import com.chenxiaoyu.bbcoin.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PriceTextView extends TextView{

	float mLastValue = 0;
	int mMaxFractionNum = 1;
	public PriceTextView(Context context) {
		super(context);

	}
	public PriceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setMaxFractionNum(int num){
		this.mMaxFractionNum = num;
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		try {
			
			if (mLastValue != 0) {
				float now = Float.valueOf(text.toString());
				float gap = Math.abs(now - mLastValue);
				String g = String.format(Locale.CHINA, "%."+ mMaxFractionNum + "f", gap);
//				float minDelta = (float) Math.pow(0.1, mMaxFractionNum);
				if (gap > 0) {
					text = text + "¡ü" + g;
				}else if (gap < 0) {
					text = text + "¡ý" + g;
				}
				mLastValue = now;
			}else {
				mLastValue = Float.valueOf(text.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.setText(text, type);
	}
	

}
