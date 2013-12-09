package com.chenxiaoyu.bbcoin;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;

public class Utils {

	public static String time2str(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
	
	
	public static String timePassed(Context context, Calendar calendar){
		
		Calendar c2 = Calendar.getInstance();
		long l = c2.getTimeInMillis()-calendar.getTimeInMillis();
		
		Calendar ret = Calendar.getInstance();
		ret.set(0, 0, 1, 0, 0, 0);
		ret.add(Calendar.MILLISECOND, (int)l);
		
		int day = ret.get(Calendar.DAY_OF_YEAR);
		int hour = ret.get(Calendar.HOUR_OF_DAY);
		int min = ret.get(Calendar.MINUTE);
		int sec = ret.get(Calendar.SECOND);
		StringBuilder builder = new StringBuilder();
		if (day >=2 ) {
			builder.append(day-1);
			builder.append( context.getString(R.string.day) );
			
		}
		if(hour != 0){
			builder.append(hour);
			builder.append(context.getString(R.string.hour));
		}
		if(min != 0){
			builder.append(min);
			builder.append(context.getString(R.string.minite));
		}
		builder.append(sec);
		builder.append(context.getString(R.string.second));
		return builder.toString();
	}
}
