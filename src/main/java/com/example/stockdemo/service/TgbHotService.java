package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.TgbStockRepository;
import com.example.stockdemo.dao.XgbStockRepository;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.TgbStock;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TgbHotService {
    Log log = LogFactory.getLog(TgbHotService.class);
    @Autowired
    TgbStockRepository tgbStockRepository;
    @Autowired
    XgbStockRepository xgbStockRepository;
    @Autowired
    RestTemplate restTemplate;
    public void dayTimeStockWorkday(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                TgbStock tgbStock = new TgbStock(code,stockName);
                List<TgbStock> list = tgbStockRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat());
                if(list!=null && list.size()>0){
                    tgbStock = list.get(0);
                }
                tgbStock.setStockType(NumberEnum.StockType.WORKDAY.getCode());
                tgbStock.setHotSort(i-9);
                tgbStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                tgbStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                tgbStock.setCreated(MyUtils.getCurrentDate());
                log.info("WORKDAY:"+code+":"+stockName);
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    tgbStock.setPlateName(xgbStock.getPlateName());
                    tgbStock.setOneFlag(xgbStock.getOpenCount());
                    tgbStock.setContinuous(xgbStock.getContinueBoardCount());
                    tgbStock.setLimitUp(1);
                }else {
                    tgbStock.setPlateName("");
                    tgbStock.setOneFlag(1);
                    tgbStock.setContinuous(0);
                    tgbStock.setLimitUp(0);
                }
                String currentPrice = currentPrice(tgbStock.getCode());
                tgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStockRepository.save(tgbStock);

            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log.info("==>重新执行");
            dayTimeStockWorkday();
        }
    }
    public void dayTimeStockHoliday(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                TgbStock tgbStock = new TgbStock(code,stockName);
                List<TgbStock> list = tgbStockRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat(MyUtils.getTomorrowDate()));
                if(list!=null && list.size()>0){
                    tgbStock = list.get(0);
                }
                tgbStock.setStockType(NumberEnum.StockType.HOLIDAY.getCode());
                tgbStock.setHotSort(i - 9);
                tgbStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                tgbStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                tgbStock.setCreated(MyUtils.getYesterdayDate());
                log.info("HOLIDAY:"+code+":"+stockName);
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    tgbStock.setPlateName(xgbStock.getPlateName());
                    tgbStock.setOneFlag(xgbStock.getOpenCount());
                    tgbStock.setContinuous(xgbStock.getContinueBoardCount());
                    tgbStock.setLimitUp(1);
                }else {
                    tgbStock.setPlateName("");
                    tgbStock.setOneFlag(1);
                    tgbStock.setContinuous(0);
                    tgbStock.setLimitUp(0);
                }
                String currentPrice = currentPrice(tgbStock.getCode());
                tgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStockRepository.save(tgbStock);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log.info("==>重新执行");
            dayTimeStockHoliday();
        }
    }
    public void closeLimitUp(){
        String urlCloseLimitUp = "https://wows-api.wallstreetcn.com/v2/sheet/board_stock?filter=true";
        try {
            Object response =  restTemplate.getForObject(urlCloseLimitUp, String.class);
            JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
            for(int i=0;i<closeLimitUp.size();i++){
                JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
                XGBStock xgbStock = new XGBStock();
                xgbStock.setCreated(MyUtils.getCurrentDate());
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
                log.info(xgbStock.getDayFormat()+":当日涨停：" + xgbStock.toString());
                xgbStockRepository.save(xgbStock);
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log.info("==>重新执行");
            closeLimitUp();
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