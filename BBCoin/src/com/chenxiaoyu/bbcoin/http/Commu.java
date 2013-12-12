package com.chenxiaoyu.bbcoin.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

import com.chenxiaoyu.bbcoin.Coin;
import com.chenxiaoyu.bbcoin.CoinStatus;
import com.chenxiaoyu.bbcoin.CoinsPrice;

import android.net.http.AndroidHttpClient;

public class Commu  {

    /**
     * 发送Http请求到Web站点
     * @param path Web站点请求地址
     * @param map Http请求参数
     * @param encode 编码格式
     * @return Web站点响应的字符串
     */
    public String sendHttpClientPost(String path,Map<String, String> map,String encode)
    {
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        if(map!=null&&!map.isEmpty())
        {
            for(Map.Entry<String, String> entry:map.entrySet())
            {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list, encode);
            HttpPost httpPost=new HttpPost(path);
            httpPost.setEntity(entity);
            HttpClient client= new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
                    System.getProperty("Mozilla/5.0"));

            HttpResponse httpResponse= client.execute(httpPost);

            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                InputStream inputStream=httpResponse.getEntity().getContent();
                return changeInputStream(inputStream,encode);
            }
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }                    
    /**
     * 把Web站点返回的响应流转换为字符串格式
     * @param inputStream 响应流
     * @param encode 编码格式
     * @return 转换后的字符串
     */
    private  String changeInputStream(InputStream inputStream,
            String encode) { 
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result="";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data,0,len);                    
                }
                result=new String(outputStream.toByteArray(),encode);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    static Commu instance;
    public static Commu getInstance(){
    	if(instance == null){
    		instance = new Commu();
    	}
    	return instance;
    }
    
//    final String URL_TRAD = "http://www.btc38.com//trade/getTradeList.php?coinname=";
//    public CoinStatus fetchCoinStatus(String coin){
//    	CoinStatus ret = null;
//    	String string = sendHttpClientPost(URL_TRAD+coin, null, "utf-8");
//    	
//    	if(string != null){
//    		ret = CoinStatus.parseJSON(string);
//    	}
//    	return ret;
//    }
    
    final String URL_PRICE = "http://ggcoin.sinaapp.com/fetchData/allPrice";
    public CoinsPrice fetchCoinsPrices(){
    	CoinsPrice ret = null;
    	String str = sendHttpClientPost(URL_PRICE, null, "utf-8");
    	if(str != null){
    		ret = CoinsPrice.parseJSON(str);
    	}
    	return ret;
    }
    
    final String URL_ALL_TRADE = "http://ggcoin.sinaapp.com/fetchData/tradeListAll";
    public List<CoinStatus> fetchAllTradeList(){
    	List<CoinStatus> ret = new ArrayList<CoinStatus>();
    	String data = sendHttpClientPost(URL_ALL_TRADE, null, "utf-8");
    	if (data != null) {		
    		try {
    			JSONObject jsonObject = new JSONObject(data); 
    			for( int i = 0 ; i < Coin.COINS.length; i++) {
    				JSONObject eachTradList = jsonObject.getJSONObject(Coin.COINS[i]);
    				CoinStatus cs = CoinStatus.parseJOSN(eachTradList);
    				cs.setCoinID(i);
    				ret.add(cs);
    			}

    		} catch (Exception e) {
    			ret = null;
    			e.printStackTrace();
    		}
		}
    	return ret;
    }
}