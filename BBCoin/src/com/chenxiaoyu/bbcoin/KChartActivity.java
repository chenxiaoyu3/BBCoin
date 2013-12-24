package com.chenxiaoyu.bbcoin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.stockchart.StockChartActivity;
import org.stockchart.core.Area;
import org.stockchart.core.Axis;
import org.stockchart.core.Axis.ILabelFormatProvider;
import org.stockchart.core.Axis.Side;
import org.stockchart.core.Theme;
import org.stockchart.series.BarSeries;
import org.stockchart.series.StockSeries;

import com.chenxiaoyu.bbcoin.http.Commu;
import com.chenxiaoyu.bbcoin.model.ChartPoint;
import com.chenxiaoyu.bbcoin.model.Coin;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class KChartActivity extends StockChartActivity{

	public static final String TAG = "KChartActivity";
	private StockSeries mPriceSeries;
	private BarSeries mVolumeSeries;
	private List<ChartPoint> mData;
	private FetchChartDataTask mTask;
	private int mCoinID;
	Area mPriceArea;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		//cxy: initChart() called first
		super.onCreate(savedInstanceState);
		mCoinID = getIntent().getIntExtra("coinID", 0);
		mPriceArea.setTitle(Coin.sGetStrName(mCoinID));
		mTask = new FetchChartDataTask();
		mTask.execute(0);
		Log.v(TAG, " onCreate");
		
	}
	
	private void populateChart()
	{
		for(ChartPoint p :mData){
			mPriceSeries.addPoint(p.o, p.h, p.l, p.c);
			
			mVolumeSeries.addPoint(0, p.v);
		}
		this.getStockChartView().invalidate();
		
	}
	@Override
	protected void initChart() {
		Log.v(TAG, "initChart");
		mPriceSeries = new StockSeries();
		mPriceSeries.setName("price");				
		mPriceSeries.setYAxisSide(Side.RIGHT);
		
		mVolumeSeries = new BarSeries();
		mVolumeSeries.setName("volume");
		mVolumeSeries.setYAxisSide(Side.RIGHT);
		
		mPriceArea = this.getStockChartView().addArea();
		mPriceArea.setHeightInPercents(0.7f);
		mPriceArea.setAutoHeight(false);
		
		mPriceArea.getRightAxis().setSize(150);
		mPriceArea.getBottomAxis().setSize(50);
		mPriceArea.getBottomAxis().setLabelFormatProvider(new TimeLableFomatProvider());
		mPriceArea.getRightAxis().setLabelFormatProvider(new PriceLabelFormatProvider());
		mPriceArea.getSeries().add(mPriceSeries);
		
		Area a = this.getStockChartView().addArea();
		a.setHeightInPercents(0.3f);
		a.setAutoHeight(false);
		a.getRightAxis().setSize(150);
		a.getBottomAxis().setSize(50);
		a.getBottomAxis().setLabelFormatProvider(new TimeLableFomatProvider());
		a.getSeries().add(mVolumeSeries);
		
		
	}

	@Override
	protected void restoreChart() {
		Log.v(TAG, "restoreChart");
		mPriceSeries = (StockSeries) this.getStockChartView().findSeriesByName("price");
		mVolumeSeries = (BarSeries) this.getStockChartView().findSeriesByName("volume");
	}
	
	class FetchChartDataTask extends AsyncTask<Object, Object, List<ChartPoint>>{

		@Override
		protected List<ChartPoint> doInBackground(Object... arg0) {
			return  Commu.getInstance().fetchTimeLine(mCoinID);
		}
		
		@Override
		protected void onPostExecute(List<ChartPoint> result) {
			if (result != null) {
				mData = result;
				populateChart();
			}
			super.onPostExecute(result);
		}
		
	}
	
	class TimeLableFomatProvider implements ILabelFormatProvider{

		public static final int TIME_LINE = 0;
		public static final int DAY_LINE = 1;
		
		private int mType;
		public TimeLableFomatProvider() {
			
		}
		
		@Override
		public String getAxisLabel(Axis sender, double value) {
			
			String ret = "";
			if (Double.isNaN(value)) {
				return ret;
			}
			Date curDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(curDate);
			
			int x = Utils.numberRound(value);
			cal.add(Calendar.HOUR_OF_DAY, x-72);
			if (mType == TIME_LINE) {
				if (x == 72 || x == 0) {
					ret = Utils.timeFormat(cal.getTime(), "HH:00");
				}else{
					ret = Utils.timeFormat(cal.getTime(), "MM/dd HH:00");
				}
			}
			return ret;
		}

		public int getmType() {
			return mType;
		}

		public void setmType(int mType) {
			this.mType = mType;
		}
		
	}
	
	class PriceLabelFormatProvider implements ILabelFormatProvider{

		@Override
		public String getAxisLabel(Axis sender, double value) {
			return String.format(Locale.CHINA, "%.3f", value);
		}
		
	}

	
}
