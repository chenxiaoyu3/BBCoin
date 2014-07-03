package com.chenxiaoyu.bbcoin;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;


public enum TimerManager {

	Instance;
	
	public String TAG = "TimerManager";
	private Timer mTimer = new Timer();;
	private volatile List<TimerTask> mTasks = new LinkedList<TimerTask>();

	
	private TimerManager() {
		
		restart();
	}
	
	public void restart(){
		
		int sec = Integer.valueOf( (String)PreferManager.Instance().get(BBCoinApp.AppContext, "auto_refresh_delta") ); 
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for(TimerTask task : mTasks){
					task.run();
				}
				
			}
		}, 0, sec*1000);
	}
	
	public void addTask(TimerTask task){
		if (task != null) {
			mTasks.add(task);
		}
	}
	public void removeTask(TimerTask task){
		if (task != null) {
			mTasks.remove(task);
		}
	}
	
	public void pause(){
		Log.v(TAG, "Pause");
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
		}
	}
	public void resume(){
		Log.v(TAG, "resume");
		restart();
	}
}
