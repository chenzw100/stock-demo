package com.example.stockdemo.service.tgb;

import com.example.stockdemo.dao.CurrentStockRepository;
import com.example.stockdemo.dao.TgbStockRepository;
import com.example.stockdemo.dao.XgbStockRepository;
import com.example.stockdemo.domain.CurrentStock;
import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.service.qt.QtService;
import com.example.stockdemo.utils.MyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component(value = "tgbNewService")
public class TgbService extends QtService {
    @Autowired
    TgbStockRepository tgbStockRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;
    @Autowired
    XgbStockRepository xgbStockRepository;

    //获取24小时的热搜数据
    public void dayDate(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            Date nextWorkDay = MyUtils.getTomorrowDate();
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                String currentPrice = getCurrentPrice(code);
                if(currentPrice == null){
                    continue;
                }
                TgbStock tgbStock = new TgbStock(code,stockName);
                tgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                List<TgbStock> list = tgbStockRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat(nextWorkDay));
                if(list!=null && list.size()>0){
                    tgbStock = list.get(0);
                }
                tgbStock.setStockType(NumberEnum.StockType.HOLIDAY.getCode());
                tgbStock.setHotSort(i - 9);
                tgbStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                tgbStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                tgbStock.setCreated(nextWorkDay);
                log.info(tgbStock.getDayFormat()+"==>HOLIDAY:"+code+":"+stockName);
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
                tgbStockRepository.save(tgbStock);
            }
        } catch (IOException e) {
            e.printStackTrace();
            /*try {
                Thread.sleep(500000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            dayDate();
            log.info("==>重新执行");*/
        }
    }
    //获取实时的数据
    public void currentDate(){
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
                String currentPrice = getCurrentPrice(code);
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

}
