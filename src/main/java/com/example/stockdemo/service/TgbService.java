package com.example.stockdemo.service;

import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.enums.NumberEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TgbService {
    Log log = LogFactory.getLog(TgbService.class);
    static Map<String, MyStock>  currentTime = new HashMap();
    public Map getHop24Stock() {
        return getHopStock(10);
    }
    public Map getHopAllStock() {
        return getHopStock(20);
    }
    public  Map getHopStock(int size){
        Map<String, MyStock>  hotOpen = new HashMap();
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=0;i<size;i++){
                Element element = elements.get(i);
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                MyStock myStock = new MyStock(code,stockName);
                if(!hotOpen.containsKey(code)){
                    hotOpen.put(code,myStock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return getHopStock(size);
        }

        return hotOpen;
    }
    public void currentTimeStock(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=0;i<10;i++){
                Element element = elements.get(i);
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                MyStock myStock = new MyStock(code,stockName);
                myStock.setStockType(NumberEnum.StockType.CURRENT.getCode());
                log.info(i+":"+code+":"+stockName);
                currentTime.put(code,myStock);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                MyStock myStock = new MyStock(code,stockName);
                myStock.setStockType(NumberEnum.StockType.DAY.getCode());
                log.info(i+":"+code+":"+stockName);
                if(currentTime.containsKey(code)){
                    myStock.setStockType(NumberEnum.StockType.COMMON.getCode());
                }
                currentTime.put(code,myStock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, MyStock> getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Map<String, MyStock> currentTime) {
        this.currentTime = currentTime;
    }
    void clearTime(){
        currentTime.clear();
    }

}
