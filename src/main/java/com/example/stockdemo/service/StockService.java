package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.SinaStock;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import java.util.List;
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
//@Component
public class StockService {
    Log log = LogFactory.getLog(StockService.class);
    private static Map<String, XGBStock> yesterdayLimitUp = new HashMap();
    private static Map<String, MyStock>  tomorrow = new HashMap();
    private static Map<String, MyStock> today = new HashMap();
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MyStockRepository myStockRepository;
    private String currentPrice(String code) {
        String url ="http://qt.gtimg.cn/q=s_"+code;
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split("~");
        if(stockObj.length<3){
            log.error(code + ":err=" + str);
            return null;
        }
        return stockObj[3];
    }

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

    public void choiceLimitUp(){
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
                    if(today.containsKey(code)){
                        myStock.setStockType(NumberEnum.StockType.COMMON.getCode());
                    }
                    today.put(code,myStock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void choice(){
        StringBuilder sb = new StringBuilder();
        sb.append("选股:<br>");
        int i=0;
        for (String code:today.keySet()){
            MyStock myStock= today.get(code);
            i++;
            log.info(i + ":" + code + ":" + myStock.getName());
            myStock.setCreated(new Date());
            myStockRepository.save(myStock);
            myStock.toChoice(sb);
        }
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
    }

    public void open(){
        for (String code:today.keySet()){
            MyStock myStock = today.get(code);
            //选出来后，新的价格新的一天
            String currentPrice = currentPrice(code);
            myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            myStock= myStockRepository.save(myStock);
            today.put(code,myStock);
        }
        if(tomorrow.isEmpty()){
            List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyyMMdd"));
            if(myStocks!=null){
                for(MyStock myStock :myStocks){
                    tomorrow.put(myStock.getCode(),myStock);
                }
            }
        }
        for (String code:tomorrow.keySet()){
            MyStock myStock = tomorrow.get(code);
            String currentPrice = currentPrice(code);
            myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            myStock = myStockRepository.save(myStock);
            tomorrow.put(code,myStock);
        }
    }
    public void close(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");

        List<MyStock> myStocksTomorrow = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyy-MM-dd"));
        if(myStocksTomorrow!=null){
            for(MyStock myStock :myStocksTomorrow){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myStock.toCloseTomorrow(sb);
                myStockRepository.save(myStock);
            }
        }
        tomorrow=today;

        sb.append("今天汇总：<br>");
        List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd"));
        if(myStocks!=null){
            for(MyStock myStock :myStocks){
                today.put(myStock.getCode(),myStock);
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myStock.toClose(sb);
                myStockRepository.save(myStock);
            }
        }
        today.clear();
        MailSendUtil.sendMail(sb.toString());
    }


}
