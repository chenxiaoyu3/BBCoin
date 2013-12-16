
package com.chenxiaoyu.bbcoin.service;



import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	public static final String E_CONTENT_TITLE = "E_CONTENT_TITLE";
	public static final String E_CONTENT = "E_CONTENT";
	public static final String E_NOTIFY_TITLE = "E_NOTIFY_TITLE";
	public static final String E_NOTIFY_BITMAP = "E_NOTIFY_BITMAP";
	final String TAG = "BroadcastReceiver";
    @SuppressWarnings("deprecation")
	@Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	
    	try {
    		Bundle bundle = intent.getExtras();
        	String contentTitle = bundle.getString(E_CONTENT_TITLE);
        	String content = bundle.getString(E_CONTENT);
        	String notifyTitle = bundle.getString(E_NOTIFY_TITLE);
//        	Bitmap bt = (Bitmap)bundle.getParcelable(E_NOTIFY_BITMAP);
        	
//        	if (bt == null) {
//				Log.w(TAG, "Big picture not cached!");
//			}
        	//Intent mainIntent = new Intent(context, MainActivity.class);
        	//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
        	NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);               
        	Notification n = new Notification.Builder(context)
        	.setContentTitle(contentTitle)
        	.setContentText(content)
        	.setAutoCancel(false)
        	.setTicker(notifyTitle)
//        	.setSmallIcon(R.drawable.alarm2)
//        	.setLargeIcon(bt)
        	//.setStyle(new Notification.BigTextStyle())
//        	.setContentIntent(pendingIntent)
        	.getNotification();

        	nm.notify(145, n);
        	Log.i(TAG, "DONE");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
}
