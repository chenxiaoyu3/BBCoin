package com.chenxiaoyu.bbcoin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.ChartPoint;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.KChartType;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.stockchart.StockChartView;
import org.stockchart.core.Area;
import org.stockchart.core.Axis;
import org.stockchart.core.Axis.ILabelFormatProvider;
import org.stockchart.core.Axis.Side;
import org.stockchart.series.BarSeries;
import org.stockchart.series.StockSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KChartActivity extends SherlockActivity {

    public static final String TAG = "KChartActivity";
    private StockChartView fStockChartView;

    private StockSeries mPriceSeries;
    private BarSeries mVolumeSeries;
    private List<ChartPoint> mData;
    private FetchChartDataTask mTask;
    private int mCoinID;
    private KChartType mChartType = KChartType.OneHour;
    Area mPriceArea, mVolumeArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        fStockChartView = new StockChartView(this);

        if (null != savedInstanceState) {
            restoreChartFromState(savedInstanceState);
        } else {
            initChart();
        }

        setContentView(fStockChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mCoinID = getIntent().getIntExtra("coinID", 0);
        mPriceArea.setTitle(Coin.sGetStrName(mCoinID));
        mTask = new FetchChartDataTask();
        mTask.execute(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Coin.getStrNameWithChs(mCoinID));
        setSupportProgress(Window.PROGRESS_END);
        Log.v(TAG, " onCreate");

    }

    @Override
    protected void onStart() {
        MobclickAgent.onEventBegin(this, "ViewKChart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        MobclickAgent.onEventEnd(this, "ViewKChart");
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void populateChart() {
        resetArea();

        for (ChartPoint p : mData) {
            mPriceSeries.addPoint(p.o, p.h, p.l, p.c);
            mVolumeSeries.addPoint(0, p.v);
        }
        mPriceArea.getBottomAxis().setLabelFormatProvider(new TimeLableFomatProvider(mChartType));
        mVolumeArea.getBottomAxis().setLabelFormatProvider(new TimeLableFomatProvider(mChartType));
        fStockChartView.invalidate();
    }

    protected void initChart() {
        Log.v(TAG, "initChart");

        mPriceArea = fStockChartView.addArea();
        mPriceArea.setHeightInPercents(0.7f);
        mPriceArea.setAutoHeight(false);

        mPriceArea.getRightAxis().setSize(150);
        mPriceArea.getBottomAxis().setSize(50);

        mPriceArea.getRightAxis().setLabelFormatProvider(new PriceLabelFormatProvider());


        mVolumeArea = fStockChartView.addArea();
        mVolumeArea.setHeightInPercents(0.3f);
        mVolumeArea.setAutoHeight(false);
        mVolumeArea.getRightAxis().setSize(150);
        mVolumeArea.getBottomAxis().setSize(50);

        resetArea();
    }

    void resetArea() {
        mPriceSeries = new StockSeries();
        mPriceSeries.setName("price");
        mPriceSeries.setYAxisSide(Side.RIGHT);
        mPriceArea.getSeries().clear();
        mPriceArea.getSeries().add(mPriceSeries);

        mVolumeSeries = new BarSeries();
        mVolumeSeries.setName("volume");
        mVolumeSeries.setYAxisSide(Side.RIGHT);
        mVolumeArea.getSeries().clear();
        mVolumeArea.getSeries().add(mVolumeSeries);
    }

    protected void restoreChart() {
        Log.v(TAG, "restoreChart");
        mPriceSeries = (StockSeries) fStockChartView.findSeriesByName("price");
        mVolumeSeries = (BarSeries) fStockChartView.findSeriesByName("volume");
    }


    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putString(TAG, fStockChartView.save());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void restoreChartFromState(Bundle state) {
        String s = state.getString(TAG);

        try {
            fStockChartView.load(s);

            restoreChart();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 1, 1, "5m")
                .setCheckable(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 2, 2, "1h")
                .setCheckable(true)
                .setChecked(true)

                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 3, 3, "1d")
                .setCheckable(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.setGroupCheckable(1, true, true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case 1:
                mChartType = KChartType.FiveMin;
                break;
            case 2:
                mChartType = KChartType.OneHour;
                break;
            case 3:
                mChartType = KChartType.OneDay;
                break;
            default:
                finish();
                break;
        }
        if (mTask == null) {
            mTask = new FetchChartDataTask(mChartType);
            mTask.execute(0);
        }
        return super.onOptionsItemSelected(item);
    }

    class FetchChartDataTask extends AsyncTask<Object, Object, List<ChartPoint>> {
        KChartType type = KChartType.OneHour;

        public FetchChartDataTask() {
        }

        public FetchChartDataTask(KChartType type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {

            setSupportProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected List<ChartPoint> doInBackground(Object... arg0) {
            return Commu.getInstance().fetchChartLine(mCoinID, type);
        }

        @Override
        protected void onPostExecute(List<ChartPoint> result) {
            if (result != null) {
                mData = result;
                populateChart();
            }
            setSupportProgressBarIndeterminateVisibility(false);
            super.onPostExecute(result);
            mTask = null;
        }

    }

    class TimeLableFomatProvider implements ILabelFormatProvider {

        private KChartType type;

        public TimeLableFomatProvider(KChartType type) {
            this.type = type;
        }

        @Override
        public String getAxisLabel(Axis sender, double value) {
            Log.v(TAG, "x:" + value);
            String ret = "";
            if (Double.isNaN(value)) {
                return ret;
            }
            Date curDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDate);

            int x = Utils.numberRound(value);

            switch (type) {
                case OneHour:
                    cal.add(Calendar.HOUR_OF_DAY, x - 72);
//				if (x == 72 || x == 0) {
//					ret = Utils.timeFormat(cal.getTime(), "HH:00");
//				}else{
//					ret = Utils.timeFormat(cal.getTime(), "MM/dd HH:00");
//				}
                    ret = Utils.timeFormat(cal.getTime(), "MM/dd HH:00");
                    break;
                case OneDay:
                    cal.add(Calendar.DAY_OF_MONTH, x - 90);
                    ret = Utils.timeFormat(cal.getTime(), "MM/dd");
                    break;
                case FiveMin:
                    cal.add(Calendar.MINUTE, x - 72);
                    ret = Utils.timeFormat(cal.getTime(), "HH:mm");
                default:
                    break;
            }

            return ret;
        }

    }

    class PriceLabelFormatProvider implements ILabelFormatProvider {

        @Override
        public String getAxisLabel(Axis sender, double value) {
            return String.format(Locale.CHINA, "%.3f", value);
        }

    }


}
