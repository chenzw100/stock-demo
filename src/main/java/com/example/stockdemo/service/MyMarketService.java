package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.DownStockRepository;
import com.example.stockdemo.dao.StrongStocksDownRepository;
import com.example.stockdemo.dao.TemperatureRepository;
import com.example.stockdemo.dao.XgbStockRepository;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//@Component
public class MyMarketService {
    Log log = LogFactory.getLog(MyMarketService.class);

    private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StrongStocksDownRepository strongStocksDownRepository;

    //3.10执行
    public void market(int type){
        Object response =  restTemplate.getForObject(market_url, String.class);
        JSONObject detailInfo = JSONObject.parseObject(response.toString()).getJSONObject("data");
        int limitDownCount = detailInfo.getInteger("limit_down_count");
        int limitUpCount = detailInfo.getInteger("limit_up_count");
        MyMarket myMarket = new MyMarket(type);
        myMarket.setRaiseUp(limitUpCount);
        myMarket.setDownUp(limitDownCount);


    }
    //3.10执行
    public void broken(MyMarket myMarket){
        Object response =  restTemplate.getForObject(broken_url, String.class);
        JSONArray arrayBroken = JSONObject.parseObject(response.toString()).getJSONArray("data");
        JSONObject jsonBrokenLast = arrayBroken.getJSONObject(arrayBroken.size()-1);
        Double limitUpBrokenCount = jsonBrokenLast.getDouble("limit_up_broken_ratio")*100;
        //temperature.setBrokenRatio(MyUtils.getCentBySinaPriceStr(decimalFormat.format(limitUpBrokenCount)));
       // myMarket.setBroken(limitUpBrokenCount);
    }

    private Integer currentTradeVal() {
        String url ="http://qt.gtimg.cn/q=s_sh000001";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split("~");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return null;
        }
        str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    private String currentContinueVal() {
        String url ="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return null;
        }
        str =stockObj[5];
        return str;
    }
}
