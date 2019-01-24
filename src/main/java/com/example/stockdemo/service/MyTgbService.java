package com.example.stockdemo.service;

import com.example.stockdemo.dao.CurrentStockRepository;
import com.example.stockdemo.dao.MyFiveTgbStockRepository;
import com.example.stockdemo.dao.MyTgbStockRepository;
import com.example.stockdemo.dao.XgbStockRepository;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.utils.MyChineseWorkDay;
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
import java.util.List;

@Component
public class MyTgbService {
    Log log = LogFactory.getLog(MyTgbService.class);
    @Autowired
    MyTgbStockRepository myTgbStockRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;
    @Autowired
    XgbStockRepository xgbStockRepository;
    @Autowired
    MyFiveTgbStockRepository myFiveTgbStockRepository;
    @Autowired
    RestTemplate restTemplate;
    public void choice(){
        choiceFive();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay());
        String end = MyUtils.getDayFormat();
        List<MyTotalStock> totalStocks =  currentStockRepository.oneDayInfo(start, end);
        for(MyTotalStock myTotalStock : totalStocks){
            MyTgbStock myTgbStock = new MyTgbStock(myTotalStock.getCode(),myTotalStock.getName());
            myTgbStock.setHotSort(myTotalStock.getTotalCount());
            myTgbStock.setHotValue(myTotalStock.getHotValue());
            myTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = currentPrice(myTotalStock.getCode());
            myTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                XGBStock xgbStock =xgbStocks.get(0);
                myTgbStock.setPlateName(xgbStock.getPlateName());
                myTgbStock.setOneFlag(xgbStock.getOpenCount());
                myTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                myTgbStock.setLimitUp(1);
            }else {
                myTgbStock.setPlateName("");
                myTgbStock.setOneFlag(1);
                myTgbStock.setContinuous(0);
                myTgbStock.setLimitUp(0);
            }
            myTgbStock.setCreated(MyUtils.getCurrentDate());

            myTgbStockRepository.save(myTgbStock);
        }
    }
    public void choiceFive(){
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, MyUtils.getCurrentDate()));
        List<MyTotalStock> totalStocks =  currentStockRepository.fiveDayInfo(start, end);
        for(MyTotalStock myTotalStock : totalStocks){
            MyFiveTgbStock myFiveTgbStock = new MyFiveTgbStock(myTotalStock.getCode(),myTotalStock.getName());
            myFiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            myFiveTgbStock.setHotValue(myTotalStock.getHotValue());
            myFiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = currentPrice(myTotalStock.getCode());
            myFiveTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                XGBStock xgbStock =xgbStocks.get(0);
                myFiveTgbStock.setPlateName(xgbStock.getPlateName());
                myFiveTgbStock.setOneFlag(xgbStock.getOpenCount());
                myFiveTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                myFiveTgbStock.setLimitUp(1);
            }else {
                myFiveTgbStock.setPlateName("");
                myFiveTgbStock.setOneFlag(1);
                myFiveTgbStock.setContinuous(0);
                myFiveTgbStock.setLimitUp(0);
            }
            myFiveTgbStock.setCreated(MyUtils.getCurrentDate());

            myFiveTgbStockRepository.save(myFiveTgbStock);
        }
    }

    public void openFive(){
        List<MyFiveTgbStock> todayStocks = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MyFiveTgbStock myStock :todayStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myFiveTgbStockRepository.save(myStock);
            }
        }
        List<MyFiveTgbStock> myStocks = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MyFiveTgbStock myStock :myStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myFiveTgbStockRepository.save(myStock);
            }
        }

    }
    public void open(){
        openFive();
        List<MyTgbStock> todayStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MyTgbStock myStock :todayStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }
        List<MyTgbStock> myStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MyTgbStock myStock :myStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }

    }
    public void close(){
        List<MyTgbStock> myStocksTomorrow = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(MyTgbStock myStock :myStocksTomorrow){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }
        List<MyTgbStock> myStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(MyTgbStock myStock :myStocks){
                String currentPrice = currentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                String code = myStock.getCode();
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code, MyUtils.getDayFormat());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    myStock.setOpenCount(xgbStock.getOpenCount());
                }else {
                    myStock.setOpenCount(-1);
                }
                myTgbStockRepository.save(myStock);
            }
        }
    }

    public void taogubaCurrent(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=0;i<10;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                String currentPrice = currentPrice(code);
                if(currentPrice == null){
                    continue;
                }
                CurrentStock currentStock = new CurrentStock(code,stockName);
                currentStock.setHotSort(i +1);
                currentStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                currentStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                currentStock.setCreated(MyUtils.getCurrentDate());
                currentStockRepository.save(currentStock);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

}