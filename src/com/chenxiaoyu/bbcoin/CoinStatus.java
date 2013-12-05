package com.chenxiaoyu.bbcoin;

import java.util.List;

import org.json.JSONTokener;

public class CoinStatus {

	List<Order> buyOrders, sellOrders;
	
	
	public static CoinStatus parseJSON(String str)
	{
		CoinStatus retCoinStat
        try {
            JSONObject jsonObject = new JSONObject(jsonString);          //����JSONObject����
            JSONArray personArray = jsonObject.getJSONArray(key);        //��ȡJSONObject�����ֵ����ֵ��һ��JSON����
            for(int i = 0; i < personArray.length(); i++) {
                JSONObject personObject = personArray.getJSONObject(i);  //���JSON�����е�ÿһ��JSONObject����
                Person person = new Person();
                int id = personObject.getInt("id");                      //���ÿһ��JSONObject�����еļ�����Ӧ��ֵ
                String name = personObject.getString("name");
                int age = personObject.getInt("age");
                person.setId(id);        //����������������ֵ����Person����
                person.setName(name);
                person.setAge(age);
                list.add(person);        //������������ÿһ��Person������ӵ�List��
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
	}
}
