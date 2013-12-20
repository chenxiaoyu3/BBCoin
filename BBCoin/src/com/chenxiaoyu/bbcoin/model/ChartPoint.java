package com.chenxiaoyu.bbcoin.model;

import java.util.Date;

import org.json.JSONArray;



public class ChartPoint {

	public Date dt;
	public double o, h, l, c, v;
	
	public static ChartPoint parse(JSONArray array){
		ChartPoint ret = new ChartPoint();
		try {
			ret.dt = new Date(array.getLong(0));
			ret.v = array.getDouble(1);
			ret.o = array.getDouble(2);
			ret.h = array.getDouble(3);
			ret.l = array.getDouble(4);
			ret.c = array.getDouble(5);
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		
		
		return ret;
	}
	
}
