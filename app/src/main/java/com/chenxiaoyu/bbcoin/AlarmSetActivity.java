package com.chenxiaoyu.bbcoin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.PriceAlarm;
import com.chenxiaoyu.bbcoin.service.AlarmManager;
import com.chenxiaoyu.bbcoin.service.BBCoinService;

public class AlarmSetActivity extends SherlockActivity {

    TextView mTitleTextView;
    EditText mLessEditText, mLargerEditText;
    int mCoinID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alarm_set);
        initID();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.set_alarm);
    }

    @Override
    protected void onStart() {
        mCoinID = getIntent().getIntExtra("COIN_ID", 0);
        mTitleTextView.setText(Coin.getStrNameWithChs(mCoinID));

        PriceAlarm f = AlarmManager.Instance.getPriceAlarm(this, mCoinID);
        if (f != null) {
            mLessEditText.setText(String.valueOf(f.lessThan));
            mLargerEditText.setText(String.valueOf(f.largerThan));
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 1, 1, "����")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 2, 2, "���")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case 1:
                if (saveAlarm())
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

    private void initID() {
        this.mTitleTextView = (TextView) findViewById(R.id.tv_alarmset_title);
        this.mLargerEditText = (EditText) findViewById(R.id.et_alarmset_largerthan);
        this.mLessEditText = (EditText) findViewById(R.id.et_alarmset_lessthan);
    }

    private boolean saveAlarm() {
        float lessThan = 0;
        try {
            lessThan = Float.valueOf(mLessEditText.getText().toString());
        } catch (Exception e) {
        }

        float largerThan = 0;
        try {
            largerThan = Float.valueOf(mLargerEditText.getText().toString());
        } catch (Exception e) {
        }

        AlarmManager.Instance.setPriceAlarm(this, mCoinID, lessThan, largerThan);
        BBCoinService.NeedStart(this);
        Toast.makeText(this, R.string.alarm_succ, Toast.LENGTH_SHORT).show();
        return true;
    }

}
