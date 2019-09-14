package com.example.stockdemo.service.xgb;

import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.service.qt.QtService;
import com.example.stockdemo.service.sina.SinaService;
import com.example.stockdemo.utils.MyChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class XgbDealDataService extends QtService{

    @Autowired
    XgbService xgbService;
    @Autowired
    DownStockRepository downStockRepository;
    @Autowired
    DownStockAverageRepository downStockAverageRepository;
    @Autowired
    XgbFiveUpStockRepository xgbFiveUpStockRepository;
    @Autowired
    SinaService sinaService;

    public void current(){
        xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
    }
    public void openFivePan(){
        xgbService.temperature(NumberEnum.TemperatureType.OPEN.getCode());
    }
    public void openPan(){
        log.info("xgb==>openPan start");
        openDown();
        log.info("xgb==>openPan end");
    }
    public void closePan(){
        log.info("xgb==>start closePan");
        fiveStatistic();
        xgbService.limitUp();
        xgbService.limitUpBroken();
        xgbService.superStock();
        closeDown();
        xgbService.temperature(NumberEnum.TemperatureType.CLOSE.getCode());
        log.info("xgb===>end closePan");
    }

    private void openDown(){
        List<DownStock> downStocks = downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat());
        int openAverage = 0;
        int openSize = downStocks.size();
        for (DownStock downStock:downStocks){
            //选出来后，新的价格新的一天
            String currentPrice = getCurrentPrice(downStock.getCode());
            downStock.setTodayOpenPrice(MyUtils.getCentByYuanStr(currentPrice));
            downStockRepository.save(downStock);
            openAverage=openAverage+downStock.getOpenBidRate();

        }

        DownStockAverage downStockAverage = queryDayFormat(MyUtils.getDayFormat());
        downStockAverage.setTodayOpenRate(MyUtils.getAverageRateCent(openAverage,openSize).intValue());
        saveDayFormat(downStockAverage);

        List<DownStock> downStocksTomorrow = downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        int openAverageTomorrow = 0;
        int openSizeTomorrow = downStocksTomorrow.size();
        if(downStocksTomorrow!=null){
            for(DownStock downStock :downStocksTomorrow){
                String currentPrice = getCurrentPrice(downStock.getCode());
                downStock.setTomorrowOpenPrice(MyUtils.getCentByYuanStr(currentPrice));
                downStockRepository.save(downStock);
                openAverageTomorrow=openAverageTomorrow+MyUtils.getCentByYuanStr(downStock.getTomorrowOpenRate());
            }
        }
        DownStockAverage downTomorrowStockAverage = queryDayFormat(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        downTomorrowStockAverage.setTodayOpenRate(MyUtils.getAverageRateCent(openAverageTomorrow, openSizeTomorrow).intValue());
        saveDayFormat(downTomorrowStockAverage);

    }
    private void closeDown(){
        List<DownStock> myStocksTomorrow = downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        int openAverageTomorrow = 0;
        int openSizeTomorrow = myStocksTomorrow.size();
        log.info("closeDown openSizeTomorrow size:"+openSizeTomorrow);
        if(myStocksTomorrow!=null){
            for(DownStock downStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(downStock.getCode());
                downStock.setTomorrowClosePrice(MyUtils.getCentByYuanStr(currentPrice));
                downStockRepository.save(downStock);
                openAverageTomorrow = openAverageTomorrow + MyUtils.getCentByYuanStr(downStock.getTomorrowCloseRate());
            }
        }

        DownStockAverage downTomorrowStockAverage = queryDayFormat(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        downTomorrowStockAverage.setTomorrowCloseRate(MyUtils.getAverageRateCent(openAverageTomorrow, openSizeTomorrow).intValue());
        saveDayFormat(downTomorrowStockAverage);
        List<DownStock> myStocks = downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat());
        int openAverage = 0;
        int openSize = myStocks.size();
        log.info("closeDown openSize size:"+openSize);
        if(myStocks!=null){
            for(DownStock downStock :myStocks){
                String currentPrice = getCurrentPrice(downStock.getCode());
                downStock.setTodayClosePrice(MyUtils.getCentByYuanStr(currentPrice));
                downStockRepository.save(downStock);
                openAverage = openAverage + MyUtils.getCentByYuanStr(downStock.getTodayCloseRate());
            }
        }

        DownStockAverage downStockAverage = queryDayFormat(MyUtils.getDayFormat());
        downStockAverage.setTodayCloseRate(MyUtils.getAverageRateCent(openAverage, openSize).intValue());
        saveDayFormat(downStockAverage);
    }

    private DownStockAverage queryDayFormat(String dayFormat){
        List<DownStockAverage> list = downStockAverageRepository.findByDayFormat(dayFormat);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        DownStockAverage downStockAverage = new DownStockAverage();
        downStockAverage.setCreated(new Date());
        downStockAverage.setDayFormat(dayFormat);
        return downStockAverage;
    }
    private DownStockAverage saveDayFormat(DownStockAverage downStockAverage){
        return downStockAverageRepository.save(downStockAverage);
    }

    public void fiveStatistic(){
        String end=MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4,MyUtils.getCurrentDate()));
        List<XgbFiveUpStock> xgbFiveUpStocks = xgbFiveUpStockRepository.findByCodeAndDayFormat(start, end);
        log.info(start+"--->xgb.fiveStatistic 5day count:"+xgbFiveUpStocks.size());
        if(xgbFiveUpStocks.size()>0){
            for (XgbFiveUpStock xgbFiveUpStock : xgbFiveUpStocks){
                SinaTinyInfoStock tinyInfoStock = sinaService.getTiny(xgbFiveUpStock.getCode());
                if(tinyInfoStock.getHighPrice()>xgbFiveUpStock.getFiveHighPrice().intValue()){
                    xgbFiveUpStock.setFiveHighPrice(tinyInfoStock.getHighPrice());
                }
                if(tinyInfoStock.getLowPrice()>xgbFiveUpStock.getFiveLowPrice().intValue()){
                    xgbFiveUpStock.setFiveLowPrice(tinyInfoStock.getLowPrice());
                }
                if(xgbFiveUpStock.getTodayOpenPrice().intValue()==10){
                    xgbFiveUpStock.setTodayOpenPrice(tinyInfoStock.getOpenPrice());
                }
                xgbFiveUpStockRepository.save(xgbFiveUpStock);
            }
        }
    }

}
