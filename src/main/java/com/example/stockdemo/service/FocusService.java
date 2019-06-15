package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.TemperatureRepository;
import com.example.stockdemo.domain.DownStock;
import com.example.stockdemo.domain.Temperature;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/6/15.
 */
public class FocusService {
    private static String limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up";
    private static String limit_up_broken ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up_broken";

    private static String limit_down="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_down";
    private static String super_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=super_stock";

    private static String yesterday_limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=yesterday_limit_up";
    private static String market_temperature="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=market_temperature";

    private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";

    @Autowired
    private TemperatureRepository temperatureRepository;
    @Autowired
    RestTemplate restTemplate;
    private String currentPrice(String code) {
        String url ="http://qt.gtimg.cn/q=s_"+code;
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split("~");
        if(stockObj.length<3){
            return null;
        }
        return stockObj[3];
    }
    public void temperature(int type)  {
        Temperature temperature = new Temperature(type);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        Object response =  restTemplate.getForObject(market_url, String.class);
        JSONObject detailInfo = JSONObject.parseObject(response.toString()).getJSONObject("data");
        int limitDownCount = detailInfo.getInteger("limit_down_count");
        int limitUpCount = detailInfo.getInteger("limit_up_count");
        temperature.setLimitDown(limitDownCount);
        temperature.setLimitUp(limitUpCount);
        response =  restTemplate.getForObject(broken_url, String.class);
        JSONArray arrayBroken = JSONObject.parseObject(response.toString()).getJSONArray("data");
        JSONObject jsonBrokenLast = arrayBroken.getJSONObject(arrayBroken.size()-1);
        Double limitUpBrokenCount = jsonBrokenLast.getDouble("limit_up_broken_ratio")*100;
        temperature.setBrokenRatio(MyUtils.getCentBySinaPriceStr(decimalFormat.format(limitUpBrokenCount)));
        temperature.setOpen(jsonBrokenLast.getInteger("limit_up_broken_count"));
        temperature.setContinueVal(currentContinueVal());
        temperature.setYesterdayShow(MyUtils.getCentByYuanStr(yesterdayVal()));
        temperatureRepository.save(temperature);
    }

    private String currentContinueVal() {
        String url ="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            return null;
        }
        str =stockObj[5];
        return str;
    }
    private String yesterdayVal() {
        String url ="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08151&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            return null;
        }
        str =stockObj[5];
        return str;
    }
}
