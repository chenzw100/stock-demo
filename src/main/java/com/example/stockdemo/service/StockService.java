package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * http://qt.gtimg.cn/q=s_sh600519
 * v_s_sh600519="1~贵州茅台~600519~358.74~-2.55~-0.71~27705~99411~~4506.49";
 1  0: 未知
 2  1: 股票名称
 3  2: 股票代码
 4  3: 当前价格
 5  4: 涨跌
 6  5: 涨跌%
 7  6: 成交量（手）
 8  7: 成交额（万）
 9  8:
 10  9: 总市值

 //https://www.cnblogs.com/skating/p/6424342.html
 //当日涨停写库
 //热门涨停
 //计算竞价金额
 */
@Component
public class StockService {
    Log log = LogFactory.getLog(StockService.class);
    private static String multi_stock_url="https://wows-api.wallstreetcn.com/v2/sheet/multi_stock";
    private static String boom_stock_url ="https://wows-api.wallstreetcn.com/v2/sheet/boom_stock";
    private static Map<String, XGBStock> yesterdayLimitUp = new HashMap();
    static Map<String, MyStock>  dayTime = new HashMap();
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MyStockRepository myStockRepository;

    public void closeLimitUp(){
        String urlCloseLimitUp = "https://wows-api.wallstreetcn.com/v2/sheet/board_stock?filter=true";
        Object response =  restTemplate.getForObject(urlCloseLimitUp, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String code = jsonArray.toArray()[0].toString().substring(0,6);
            xgbStock.setCode(code);
            xgbStock.setOpenCount(Integer.parseInt(jsonArray.toArray()[11].toString()));
            xgbStock.setContinueBoardCount(Integer.parseInt(jsonArray.toArray()[12].toString()));
            xgbStock.setPrice(jsonArray.toArray()[3].toString());
            String down = jsonArray.toArray()[4].toString();
            int downRate= MyUtils.getCentBySinaPriceStr(down);
            xgbStock.setDownRate(downRate);
            log.info(xgbStock.toString());
            yesterdayLimitUp.put(code,xgbStock);
        }
    }

    public void dayTimeStock(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                XGBStock xgbStock = yesterdayLimitUp.get(code.substring(2, 8));
                log.info(i+":"+code+":"+stockName);
                if(xgbStock!=null){
                    MyStock myStock = new MyStock(code,stockName);
                    myStock.setStockType(NumberEnum.StockType.DAY.getCode());
                    myStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(xgbStock.getPrice()));
                    myStock.setContinuous(xgbStock.getContinueBoardCount().toString());
                    myStock.setOpenCount(xgbStock.getOpenCount());
                    if(dayTime.containsKey(code)){
                        myStock.setStockType(NumberEnum.StockType.COMMON.getCode());
                    }
                    dayTime.put(code,myStock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void todayChoice(){
        int i=0;
        for (String code:dayTime.keySet()){
            MyStock myStock= dayTime.get(code);
            i++;
            log.info(i + ":" + code + ":" + myStock.getName());
            myStock.setCreated(new Date());
            myStock= myStockRepository.save(myStock);
        }
    }


}
