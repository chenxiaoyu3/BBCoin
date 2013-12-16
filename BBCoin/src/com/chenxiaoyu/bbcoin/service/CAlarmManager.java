package com.chenxiaoyu.bbcoin.service;
///**
// *	chenxiaoyu<chenxiaoyu@gmail>
// *	2013-4-7
//*/
//package com.chenxiaoyu.bbcoin.alarm;
//
//
//import java.util.Calendar;
//import java.util.Date;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.widget.Toast;
//
//import com.chen.app.cshare.R;
//import com.chen.app.cshare.Util;
//import com.chen.httpimage.HttpImageManager;
//import com.chen.httpimage.HttpImageManager.LoadRequest;
//import com.dongxuexidu.douban4j.model.event.DoubanEventEntryObj;
//
//public enum CAlarmManager {
//	Instance;
//	/**
//	 * 
//	 * @param context
//	 * @param event
//	 * @param time, null if event.startTime be used
//	 */
//	public void addAlarm(final Context context, final DoubanEventEntryObj event, Calendar time)
//	{
//		if (time == null) {
//			time = Calendar.getInstance();
//			time.setTime(Util.parseDoubanTimeString(event.getWhen().getStartTime()));
//		}
//		final Calendar theTime = time;
//		Bitmap bt = HttpImageManager.getInstance().loadImage(new HttpImageManager.LoadRequest(
//				Uri.parse(event.getLinkByRel("image")),
//				new HttpImageManager.OnLoadResponseListener() {
//					
//					@Override
//					public void onLoadResponse(LoadRequest r, Bitmap data) {
//						// TODO Auto-generated method stub
//						addAlerm(context, event, data, theTime);
//						
//					}
//					
//					@Override
//					public void onLoadProgress(LoadRequest r, long totalContentSize,
//							long loadedContentSize) {
//						
//					}
//					
//					@Override
//					public void onLoadError(LoadRequest r, Throwable e) {
//						addAlerm(context, event, null, theTime);
//					}
//				}));
//		if (bt != null) {
//			addAlerm(context, event, bt, theTime);
//		}
//
//	}
//	
//	private void addAlerm(Context context, DoubanEventEntryObj event, Bitmap bitmap, Calendar time)
//	{
//		String title = "Conttttttent";
//		String conent = "content";
//		String trick = "";
//		if (event != null) {
//			title = event.getTitle();
//			conent = Util.formatDate(context, Util.parseDoubanTimeString(event.getWhen().getStartTime()));
//			trick = title;
//		}
//		Intent intent = new Intent(context, AlarmReceiver.class);
//		intent.putExtra(AlarmReceiver.E_CONTENT, conent);
//		intent.putExtra(AlarmReceiver.E_CONTENT_TITLE, title);
//		intent.putExtra(AlarmReceiver.E_NOTIFY_TITLE, trick);
//		intent.putExtra(AlarmReceiver.E_NOTIFY_BITMAP, bitmap);
//	    PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//	    
////	    Calendar calendar = Calendar.getInstance();
////	    calendar.setTime(Util.parseDoubanTimeString(event.getWhen().getStartTime()));
////	    calendar.add(Calendar.HOUR, -1);
//	    
//	    AlarmManager alarm = (AlarmManager)context.getSystemService(Service.ALARM_SERVICE);
//	    alarm.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender);
//	    String s = context.getString(R.string.alarm_added, Util.formatDate(context, time.getTime()));
//	    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
//	}
//	
//
//}
