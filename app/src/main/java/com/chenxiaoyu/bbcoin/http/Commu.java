package com.chenxiaoyu.bbcoin.http;

import com.chenxiaoyu.bbcoin.model.ChartPoint;
import com.chenxiaoyu.bbcoin.model.Coin;
import com.chenxiaoyu.bbcoin.model.CoinStatus;
import com.chenxiaoyu.bbcoin.model.CoinsPrice;
import com.chenxiaoyu.bbcoin.model.KChartType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Commu {

//    /**
//     * ����Http����Webվ��
//     * @param path Webվ�������ַ
//     * @param map Http�������
//     * @param encode �����ʽ
//     * @return Webվ����Ӧ���ַ�
//     */
//    public String sendHttpClientPost(String path,Map<String, String> map,String encode)
//    {
//    	path = "http://baidu.com";
//        List<NameValuePair> list=new ArrayList<NameValuePair>();
//        if(map!=null&&!map.isEmpty())
//        {
//            for(Map.Entry<String, String> entry:map.entrySet())
//            {
//                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//            }
//        }
//        try {
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
//            HttpPost httpPost = new HttpPost(path);
////            httpPost.setEntity(entity);
////            httpPost.setHeader("Accept","application/json");
//            HttpClient client= new DefaultHttpClient();
//            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
//                    System.getProperty("Mozilla/5.0"));
//            
//            HttpResponse httpResponse= client.execute(httpPost);
//
//            if(httpResponse.getStatusLine().getStatusCode()==200)
//            {
//                InputStream inputStream=httpResponse.getEntity().getContent();
//                return changeInputStream(inputStream,encode);
//            }
//            
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        return null;
//    }

    public String sendHttpClientGet(String path) {
        try {
            HttpGet httpGet = new HttpGet(path);
            httpGet.setHeader("Accept", "application/json");
            HttpClient client = new DefaultHttpClient();

            HttpResponse httpResponse = client.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                return changeInputStream(inputStream, "utf-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String sendHttpClientGet2(String path) {
        try {
            HttpGet httpGet = new HttpGet(path);

            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Intel Mac OS X 10.6; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
            HttpResponse httpResponse = client.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                return changeInputStream(inputStream, "utf-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }

        return null;
    }

    /**
     * ��Webվ�㷵�ص���Ӧ��ת��Ϊ�ַ��ʽ
     *
     * @param inputStream ��Ӧ��
     * @param encode      �����ʽ
     * @return ת������ַ�
     */
    private String changeInputStream(InputStream inputStream,
                                     String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), encode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    static Commu instance;

    public static Commu getInstance() {
        if (instance == null) {
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

    final String URL_BASE = "http://ggcoin.sinaapp.com/";
    final String URL_PRICE_BUY = URL_BASE + "API/price/buy";
    final String URL_PRICE_SELL = URL_BASE + "API/price/sell";

    public CoinsPrice fetchCoinsBuyPrice() {
        CoinsPrice ret = null;
        String str = sendHttpClientGet(URL_PRICE_BUY);
        if (str != null) {
            ret = CoinsPrice.parseJSON(str);
        }
        return ret;
    }

    public CoinsPrice fetchCoinsSellPrice() {
        CoinsPrice ret = null;
        String str = sendHttpClientGet(URL_PRICE_SELL);
        if (str != null) {
            ret = CoinsPrice.parseJSON(str);
        }
        return ret;
    }

    final String URL_ALL_TRADE = URL_BASE + "API/trade_list";

    public List<CoinStatus> fetchAllTradeList() {
        List<CoinStatus> ret = new ArrayList<CoinStatus>();
        String data = sendHttpClientGet(URL_ALL_TRADE);
        if (data != null) {
            try {


                JSONObject jsonObject = new JSONObject(data);
                Date t = new Date(jsonObject.getLong("updatetime") * 1000);
                for (int i = 0; i < Coin.COINS.length; i++) {
                    JSONObject eachTradList = jsonObject.getJSONObject(Coin.COINS[i]);
                    CoinStatus cs = CoinStatus.parseJOSN(eachTradList);
                    cs.setCoinID(i);
                    cs.updateTime = t;
                    ret.add(cs);
                }

            } catch (Exception e) {
                ret = null;
                e.printStackTrace();
            }
        }
        return ret;
    }

    final String URL_SINGLE_TRADE = URL_BASE + "API/single_trade_list/";

    public CoinStatus fetchSingleTradeList(int coinID) {
        CoinStatus ret = null;
        String data = sendHttpClientGet(URL_SINGLE_TRADE + Coin.sGetStrName(coinID));
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                ret = CoinStatus.parseJOSN(jsonObject);
                ret.setCoinID(coinID);
                ret.updateTime = new Date();
            } catch (Exception e) {
                ret = null;
                e.printStackTrace();
            }
        }
        return ret;
    }

    final String URL_K_1H = "http://www.btc38.com/trade/getTradeTimeLine.php?coinname=";
    final String URL_K_5M = "http://www.btc38.com/trade/getTrade5minLine.php?coinname=";
    final String URL_K_1D = "http://www.btc38.com/trade/getTradeDayLine.php?coinname=";

    public List<ChartPoint> fetchChartLine(int coinID, KChartType type) {
        List<ChartPoint> ret = new ArrayList<ChartPoint>();
        String uu = "";
        switch (type) {
            case OneHour:
                uu = URL_K_1H;
                break;
            case OneDay:
                uu = URL_K_1D;
                break;
            case FiveMin:
                uu = URL_K_5M;
            default:
                break;
        }
        String data = sendHttpClientGet2(uu + Coin.sGetStrName(coinID));
        if (data != null) {
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONArray a = array.getJSONArray(i);
                    ChartPoint p = ChartPoint.parse(a);
                    ret.add(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ret = null;
            }
        }
        return ret;
    }
}