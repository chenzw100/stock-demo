package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;

@Component
public class MarketService {
    Log log = LogFactory.getLog(MarketService.class);
    public static String  temperatureRecord = "";
    private static String temperature_url = "https://wows-api.wallstreetcn.com/v2/sheet/market_temperature?date=";
    private static String limit_url = "https://wows-api.wallstreetcn.com/statis_data/min_quote_change/limit?date=";
    private static String normal_url = "https://wows-api.wallstreetcn.com/statis_data/min_quote_change/normal?date=";
    private static String kline_url = "https://wows-api.wallstreetcn.com/sheet/min_kline?kline_type=a-stock-behavior-kline&date=";
    @Autowired
    private RestTemplate restTemplate;
    //下午9:45-15:45点后执行
    public String temperature()  {
        Date date = MyUtils.getCurrentDate();
        String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        String dateParam = DateFormatUtils.format(date, "yyyyMMdd");
        StringBuilder sb = new StringBuilder();

        String urlYesterday = kline_url+dateParam;
        Object response =  restTemplate.getForObject(urlYesterday, String.class);
        JSONArray arrayYesterday = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("datas");
        JSONObject jsonYesterdayLast = arrayYesterday.getJSONObject(arrayYesterday.size()-1);
        Double dYesterday = jsonYesterdayLast.getDouble("value")*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        sb.append(dateStr+"==>> [昨日表现:").append(decimalFormat.format(dYesterday));

        String urlTemperature = temperature_url+dateParam;
        response =  restTemplate.getForObject(urlTemperature,String.class);
        String temperature = JSONObject.parseObject(response.toString()).getJSONObject("data").getString("temperature");
        sb.append("] [现在温度:").append(temperature);

        String urlLimit = limit_url+dateParam;
        response =  restTemplate.getForObject(urlLimit,String.class);
        JSONArray arrayLimit= JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("value");
        JSONArray vLimit = (JSONArray) arrayLimit.get(arrayLimit.size() - 1);
        Object down = vLimit.toArray()[2];
        Object raise = vLimit.toArray()[4];
        Object open = vLimit.toArray()[6];
        sb.append("] [涨停:").append(raise).append(", 跌停:").append(down).append(", 炸版:").append(open);

        String urlNormal = normal_url+dateParam;
        response =  restTemplate.getForObject(urlNormal,String.class);
        JSONArray arrayNormal= JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("value");
        JSONArray vNormal = (JSONArray) arrayNormal.get(arrayNormal.size() - 1);
        Object downNormal = vNormal.toArray()[2];
        Object raiseNormal = vNormal.toArray()[1];
        Object openNormal = vNormal.toArray()[6];
        sb.append("] [涨:").append(raiseNormal).append(", 跌:").append(downNormal).append(", 炸版:").append(openNormal).append("]<br>");
        String record=sb.toString();
        log.info("===>>record:"+record);
        temperatureRecord =temperatureRecord +record;
        return record;
    }
    public void clearTemperature()  {
        temperatureRecord ="";
    }


}
