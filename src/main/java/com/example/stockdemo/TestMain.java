package com.example.stockdemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.utils.HttpTookit;
import com.example.stockdemo.utils.WeiboClient;
import org.springframework.http.HttpHeaders;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

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
        /*JSONArray arrayYesterday = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("datas");
        JSONObject jsonYesterdayLast = arrayYesterday.getJSONObject(arrayYesterday.size()-1);
        Double dYesterday = jsonYesterdayLast.getDouble("value")*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        System.out.printf(decimalFormat.format(dYesterday));*/
        /*WeiboClient  weiboClient = new WeiboClient();
        String r = "https://passport.weibo.cn/signin/welcome?entry=mweibo&r=https://m.weibo.cn/api/container/getIndex?containerid=230945_-_Finance_Cardlist_Ranklist&page=1";
        weiboClient.get("https://m.weibo.cn/api/container/getIndex?containerid=230945_-_Finance_Cardlist_Ranklist&page=1");*/
        String passporturl = "https://passport.weibo.cn/sso/login";
        String r="https://m.weibo.cn/";
        Map<String, String> headers = new HashMap();
        headers.put(HttpHeaders.ACCEPT, "*/*");
        headers.put(HttpHeaders.ORIGIN, "https://passport.weibo.cn");
        headers.put(HttpHeaders.REFERER, r);
        headers.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

        Map<String, String> params = new HashMap();
        params.put("username", "15002838763");
        params.put("password", "czw_12345");
        params.put("savestate", "1");
        params.put("r", r);
        params.put("ec", "0");
        params.put("pagerefer", r);
        params.put("entry", "mweibo");
        params.put("wentry","");
        params.put("loginfrom","");
        params.put("client_id","");
        params.put("code","");
        params.put("qq","");
        params.put("mainpageflag","1");
        params.put("hff","");
        params.put("hfp","");
        String str =  HttpTookit.sendHttpsPost(passporturl,params,headers);
        System.out.println("--->"+str);

    }
}
