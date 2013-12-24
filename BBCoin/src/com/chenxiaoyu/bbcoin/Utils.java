package com.chenxiaoyu.bbcoin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class Utils {

	public static String time2str(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
	public static String timeFormat(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
		return sdf.format(date);
		
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
	
	public static String timePassed(Context context, Date date){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return timePassed(context, calendar);
	}
	
	public static int timeCompareTo(Date d1, Date d2){
		if (d1 == null || d2 == null) {
			return 0 ;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		
		return c1.compareTo(c2);
	}
	
	public static int numberRound(double d){
		return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	public static int getScale(double v){
        if (v!=v || v == Double.POSITIVE_INFINITY || v == Double.NEGATIVE_INFINITY)
            return 0;//throw exception or return any other stuff

        BigDecimal d = new BigDecimal(v);
        return Math.max(0, d.stripTrailingZeros().scale());
    }
}
