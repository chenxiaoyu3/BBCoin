package com.chenxiaoyu.bbcoin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.chenxiaoyu.bbcoin.MainActivity;
import com.chenxiaoyu.bbcoin.PreferManager;
import com.chenxiaoyu.bbcoin.R;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.model.PriceAlarm;

public enum AlarmManager {
    Instance;

    AlarmManager() {

    }

    /**
     * @param coinID
     * @param lessThan  set 0 if want it clear
     * @param lagerThan
     */
    public void setPriceAlarm(Context context, int coinID, float lessThan, float largerThan) {
        PreferManager.Instance().set(context, coinID + "_price_lessthan", lessThan);
        PreferManager.Instance().set(context, coinID + "_price_largerthan", largerThan);
    }

    public PriceAlarm getPriceAlarm(Context context, int coinID) {
        PriceAlarm ret = null;
        Float a = (Float) PreferManager.Instance().get(context, coinID + "_price_lessthan");
        Float b = (Float) PreferManager.Instance().get(context, coinID + "_price_largerthan");
        if (a != null && b != null) {
            ret = new PriceAlarm();
            ret.coinID = coinID;
            ret.lessThan = a;
            ret.largerThan = b;
        }
        return ret;
    }

    public boolean needAlarm(Context context) {
        for (int i = 0; i < Coin.COINS.length; i++) {
            PriceAlarm pa = getPriceAlarm(context, i);
            if (pa != null && (pa.lessThan != 0 || pa.largerThan != 0)) {
                return true;
            }
        }
        return false;
    }

    public void doNotify(Context context, int coinID, String title, String content, String ticker) {
        int flag = Notification.DEFAULT_ALL;
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        Intent i = new Intent(context, BBCoinService.class);
        i.putExtra(BBCoinService.REQ_CANCEL_ALARM, coinID);
        PendingIntent calIntent = PendingIntent.getService(context, 1, i, PendingIntent.FLAG_ONE_SHOT);
        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setTicker(ticker)
                .setDefaults(flag)
                .setSmallIcon(R.drawable.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentIntent(intent)
                .addAction(R.drawable.umeng_update_close_bg_normal, context.getString(R.string.cancel_this_alarm), calIntent)
                .build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(coinID, n);
    }

    public void doCheckAndAlarm(Context context, CoinsPrice cp) {
        try {
            for (int i = 0; i < Coin.COINS.length; i++) {
                PriceAlarm a = AlarmManager.Instance.getPriceAlarm(context, i);
                if (a != null) {
                    if (a.lessThan > 0 && a.lessThan >= cp.prices.get(i).getPrice()) {
                        String ti = Coin.sGetStrName(i) + " < " + a.lessThan;
                        String content = Coin.sGetStrName(i) + " : ��" + cp.prices.get(i).getPrice();
                        doNotify(context, i, ti, content, "Oops! " + ti);
                    }
                    if (a.largerThan > 0 && a.largerThan <= cp.prices.get(i).getPrice()) {
                        String ti = Coin.sGetStrName(i) + " > " + a.largerThan;
                        String content = Coin.sGetStrName(i) + " : ��" + cp.prices.get(i).getPrice();
                        doNotify(context, i, ti, content, "Yay! " + ti);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

}
