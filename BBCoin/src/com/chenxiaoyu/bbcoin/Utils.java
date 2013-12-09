package com.chenxiaoyu.bbcoin;

import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static String time2str(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
	
	public static int secPassed(Date date){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		
		Calendar c2 = Calendar.getInstance();
		long l = c2.getTimeInMillis()-c1.getTimeInMillis();
		int secs = Long.valueOf(l / (1000)).intValue();
		return secs;
	}
}
