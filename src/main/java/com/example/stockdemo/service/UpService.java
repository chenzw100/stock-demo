package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.MyStock;
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
@Component
public class UpService {
    Log log = LogFactory.getLog(UpService.class);
    private static Map<String, XGBStock> yesterdayLimitUp = new HashMap();
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
        yesterdayLimitUp.clear();
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
            JSONArray jsonArrayPlate = jsonArray.getJSONArray(15);
            String plateName ="";
            for(int j=0;j<jsonArrayPlate.size();j++){
                plateName = plateName+","+jsonArrayPlate.getJSONObject(j).getString("plate_name");
            }
            plateName =plateName.substring(1,plateName.length());
            xgbStock.setPlateName(plateName);
           // System.out.println(plateName);
            log.info("当日涨停："+xgbStock.toString());
            yesterdayLimitUp.put(code,xgbStock);
        }
    }

    public void taoguba(){
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
                log.info(i+"，taoguba:"+code+":"+stockName);
                if(xgbStock!=null){
                    MyStock myStock = new MyStock(code,stockName);
                    myStock.setHotSort(i);
                    myStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(xgbStock.getPrice()));
                    myStock.setContinuous(xgbStock.getContinueBoardCount().toString());
                    myStock.setOneFlag(xgbStock.getOpenCount());
                    myStock.setPlateName(xgbStock.getPlateName());
                    myStock.setStockType(NumberEnum.StockType.DAY.getCode());
                    today.put(code,myStock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void taogubaCurrent(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=0;i<10;i++){
                Element element = elements.get(i);
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                XGBStock xgbStock = yesterdayLimitUp.get(code.substring(2, 8));
                log.info(i + "，taogubaCurrent:" + code + ":" + stockName);
                if(xgbStock!=null){
                    MyStock myStock = new MyStock(code,stockName);
                    myStock.setStockType(NumberEnum.StockType.CURRENT.getCode());
                    myStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(xgbStock.getPrice()));
                    if(today.containsKey(code)){
                        MyStock todayStock= today.get(code);
                        if(todayStock.getStockType().intValue() !=NumberEnum.StockType.CURRENT.getCode()){
                            todayStock.setStockType(NumberEnum.StockType.COMMON.getCode());
                            today.put(code, todayStock);
                        }
                    }else {
                        myStock.setContinuous(xgbStock.getContinueBoardCount().toString());
                        myStock.setOneFlag(xgbStock.getOpenCount());
                        myStock.setPlateName(xgbStock.getPlateName());
                        myStock.setHotSort(i);
                        today.put(code, myStock);
                    }
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
            log.info(i + "选股:" + code + ":" + myStock.getName());
            myStock.setCreated(new Date());
            myStockRepository.save(myStock);
            myStock.toChoice(sb);
        }
        today.clear();
        //log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
    }

    public void open(){
        List<MyStock> todayStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MyStock myStock :todayStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myStockRepository.save(myStock);
            }
        }
        List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MyStock myStock :myStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myStockRepository.save(myStock);
            }
        }

    }
    public void close(){
        closeLimitUp();
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");
        List<MyStock> myStocksTomorrow = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(MyStock myStock :myStocksTomorrow){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myStock.toCloseTomorrow(sb);
                myStockRepository.save(myStock);
            }
        }
        sb.append("今天汇总：<br>");
        List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(MyStock myStock :myStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                String code = myStock.getCode();
                XGBStock xgbStock = yesterdayLimitUp.get(code.substring(2, 8));
                if(xgbStock!=null){
                    myStock.setOpenCount(xgbStock.getOpenCount());
                }else {
                    myStock.setOpenCount(-1);
                }
                myStock.toClose(sb);
                myStockRepository.save(myStock);
            }
        }
        MailSendUtil.sendMail(sb.toString());
    }


}
