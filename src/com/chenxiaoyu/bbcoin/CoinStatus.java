package com.chenxiaoyu.bbcoin;

import java.util.List;

import org.json.JSONTokener;

public class CoinStatus {

	List<Order> buyOrders, sellOrders;
	
	
	public static CoinStatus parseJSON(String str)
	{
		CoinStatus retCoinStat
        try {
            JSONObject jsonObject = new JSONObject(jsonString);          //创建JSONObject对象
            JSONArray personArray = jsonObject.getJSONArray(key);        //获取JSONObject对象的值，该值是一个JSON数组
            for(int i = 0; i < personArray.length(); i++) {
                JSONObject personObject = personArray.getJSONObject(i);  //获得JSON数组中的每一个JSONObject对象
                Person person = new Person();
                int id = personObject.getInt("id");                      //获得每一个JSONObject对象中的键所对应的值
                String name = personObject.getString("name");
                int age = personObject.getInt("age");
                person.setId(id);        //将解析出来的属性值存入Person对象
                person.setName(name);
                person.setAge(age);
                list.add(person);        //将解析出来的每一个Person对象添加到List中
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
	}
}
