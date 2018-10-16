package com.example.stockdemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by laikui on 2018/10/16.
 */
public class TestMain {

    public static void main(String[] args) {
/*
        Float yesterday =Float.parseFloat("9.920");
        Float opening =Float.parseFloat("8.490");
        Float rate = (opening-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        System.out.println(rate+":"+ decimalFormat.format(rate));*/
        String response = "{\"code\":200,\"data\":{\"datas\":[{\"filter_value\":0.028168063716278283,\"time_stamp\":1539653400,\"value\":0.028168063716278283},{\"filter_value\":0.004190395916672944,\"time_stamp\":1539672420,\"value\":0.004190395916672944},{\"filter_value\":0.005608072580746405,\"time_stamp\":1539672720,\"value\":0.005608072580746405},{\"filter_value\":0.005129992989323131,\"time_stamp\":1539672780,\"value\":0.005129992989323131},{\"filter_value\":0.004862421209196327,\"time_stamp\":1539672840,\"value\":0.004862421209196327},{\"filter_value\":0.004990599383274228,\"time_stamp\":1539672900,\"value\":0.004990599383274228},{\"filter_value\":0.004200387583773045,\"time_stamp\":1539672960,\"value\":0.004200387583773045},{\"filter_value\":0.0040543927439939155,\"time_stamp\":1539673020,\"value\":0.0040543927439939155},{\"filter_value\":0.004054392743993917,\"time_stamp\":1539673080,\"value\":0.004054392743993917},{\"filter_value\":0.004054392743993917,\"time_stamp\":1539673140,\"value\":0.004054392743993917},{\"filter_value\":0.0038013940852724507,\"time_stamp\":1539673200,\"value\":0.0038013940852724507}],\"pre_close_px\":{\"filter_pre_close\":0.0133086321900182,\"pre_close\":0.0133086321900182}}}";
        JSONArray arrayYesterday = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("datas");
        JSONObject jsonYesterdayLast = arrayYesterday.getJSONObject(arrayYesterday.size()-1);
        Double dYesterday = jsonYesterdayLast.getDouble("value")*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        System.out.printf(decimalFormat.format(dYesterday));

    }
}
