package com.chenxiaoyu.bbcoin;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.PriceAlarm;
import com.chenxiaoyu.bbcoin.service.AlarmManager;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmSetActivity extends SherlockActivity{

	TextView mTitleTextView;
	EditText mLessEditText, mLargerEditText;
	int mCoinID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_alarm_set);
		initID();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	protected void onStart() {
		mCoinID = getIntent().getIntExtra("COIN_ID", 0);
		mTitleTextView.setText(Coin.sGetStrName(mCoinID));
		
		PriceAlarm f = AlarmManager.Instance.getPriceAlarm(this, mCoinID);
		if (f != null) {
			mLessEditText.setText(String.valueOf(f.lessThan));
			mLargerEditText.setText(String.valueOf(f.largerThan));
		}
		super.onStart();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(1, 1, 1,"Save")
        .setIcon(R.drawable.ic_cab_done_holo_light)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(1, 2, 2, "Clear");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		switch (id) {
		case 1:
			saveAlarm();
			finish();
			break;
		case 2:
			mLessEditText.setText("0");
			mLargerEditText.setText("0");
			saveAlarm();
			finish();
			break;
		default:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initID(){
		this.mTitleTextView = (TextView)findViewById(R.id.tv_alarmset_title);
		this.mLargerEditText = (EditText)findViewById(R.id.et_alarmset_largerthan);
		this.mLessEditText = (EditText)findViewById(R.id.et_alarmset_lessthan);
	}
	
	private boolean saveAlarm(){
		float lessThan = 0;
		try {
			lessThan = Float.valueOf(mLessEditText.getText().toString());
		} catch (Exception e) {}
		
		float largerThan = 0;
		try {
			lessThan = Float.valueOf(mLargerEditText.getText().toString());
		} catch (Exception e) {}
		AlarmManager.Instance.setPriceAlarm(this, mCoinID, lessThan, largerThan);
		Toast.makeText(this, R.string.alarm_succ, Toast.LENGTH_SHORT).show();
		return true;
	}

}
