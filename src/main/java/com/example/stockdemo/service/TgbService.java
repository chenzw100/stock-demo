package com.example.stockdemo.service;

import com.example.stockdemo.domain.MyStock;
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
        }

        return hotOpen;
    }

}
