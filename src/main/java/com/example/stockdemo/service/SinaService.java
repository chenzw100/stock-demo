package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.domain.MyStock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SinaService {
    Log log = LogFactory.getLog(SinaService.class);
    @Autowired
    private RestTemplate restTemplate;
    public Map getHop24Stock() {
        Map<String, MyStock>  hotOpen = new HashMap();
        for(int i=1;i<3;i++){
            weiboRequest(i,hotOpen);
        }
        return hotOpen;
    }
    public Map<String, MyStock> weiboRequest(int page,Map<String, MyStock>  hotOpen){
        String url ="https://m.weibo.cn/api/container/getIndex?containerid=230945_-_Finance_Cardlist_Ranklist&page="+page;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Upgrade-Insecure-Requests", "1");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        requestHeaders.add("X-DevTools-Emulate-Network-Conditions-Client-Id", "978DD05B2FC0557C900FCD244B4EAAEA");
        requestHeaders.add("Accept", "application/json, text/plain, */*");
        requestHeaders.add("Referer","https://m.weibo.cn/p/tabbar?containerid=230771_-_ZUHE_INDEX");
        List<String> cookieList = new ArrayList<String>();
        cookieList.add("MLOGIN=1");
        cookieList.add("WEIBOCN_FROM=1110006030");
        cookieList.add("_T_WM=");
        cookieList.add("SUBP=");
        cookieList.add("ALF=");
        cookieList.add("SSOLoginState=");
        cookieList.add("SUHB=");
        cookieList.add("M_WEIBOCN_PARAMS=");
        cookieList.add("SCF=");
        cookieList.add("SUB=");

        requestHeaders.put(HttpHeaders.COOKIE,cookieList); //将cookie放入header
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String result = response.getBody();

        JSONArray jsonArray = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("cards");
        JSONArray ggArray =null;
        if(page==2){
            ggArray =jsonArray.getJSONObject(0).getJSONArray("card_group");
            for(int i=0;i<10;i++){
                JSONObject stockObj =ggArray.getJSONObject(i);
                String codeStr = stockObj.getString("itemid");//230677sz000979
                String code = codeStr.substring(codeStr.length()-8,codeStr.length());
                MyStock myStock = new MyStock(code,stockObj.getString("title_sub"));
                if(!hotOpen.containsKey(code)){
                    hotOpen.put(code,myStock);
                }
            }
        }else if(page==1) {
            ggArray =jsonArray.getJSONObject(1).getJSONArray("card_group");
            for(int i=1;i<11;i++){
                JSONObject stockObj =ggArray.getJSONObject(i);
                String codeStr = stockObj.getString("itemid");//230677sz000979
                String code = codeStr.substring(codeStr.length() - 8, codeStr.length());
                MyStock myStock = new MyStock(code,stockObj.getString("title_sub"));
                if(!hotOpen.containsKey(code)){
                    hotOpen.put(code,myStock);
                }
            }
        }
        return hotOpen;
    }

}
