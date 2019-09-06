package com.example.stockdemo.service.tgb;

import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.service.qt.QtService;
import com.example.stockdemo.service.sina.SinaService;
import com.example.stockdemo.utils.MyChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class TgbDealDataService extends QtService{
    @Autowired
    TgbStockRepository tgbStockRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;
    @Autowired
    XgbStockRepository xgbStockRepository;
    @Autowired
    FiveTgbStockRepository fiveTgbStockRepository;
    @Autowired
    MyFiveTgbStockRepository myFiveTgbStockRepository;
    @Autowired
    MeStockRepository meStockRepository;
    @Autowired
    MyTgbStockRepository myTgbStockRepository;
    @Autowired
    SinaService sinaService;

    public void prePan(){
        choiceFive();
        choiceCurrent();
    }
    public void openPan(){
        open();
        openCurrent();
    }
    public void closePan(){
        close();
        closeCurrent();
        fiveStatistic();
        myFiveStatistic();
    }

    private void choiceFive(){
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, MyUtils.getCurrentDate()));
        List<MyTotalStock> totalStocks =  tgbStockRepository.stockInfo(start, end);
        log.info("five hot size:"+totalStocks.size());
        for(MyTotalStock myTotalStock : totalStocks){
            FiveTgbStock fiveTgbStock = new FiveTgbStock(myTotalStock.getCode(),myTotalStock.getName());
            fiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            fiveTgbStock.setHotValue(myTotalStock.getHotValue());
            fiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
            fiveTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                XGBStock xgbStock =xgbStocks.get(0);
                fiveTgbStock.setPlateName(xgbStock.getPlateName());
                fiveTgbStock.setOneFlag(xgbStock.getOpenCount());
                fiveTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                fiveTgbStock.setLimitUp(1);
            }else {
                fiveTgbStock.setPlateName("");
                fiveTgbStock.setOneFlag(1);
                fiveTgbStock.setContinuous(0);
                fiveTgbStock.setLimitUp(0);
            }
            fiveTgbStock.setCreated(MyUtils.getCurrentDate());
            FiveTgbStock fiveTgbStockTemp =fiveTgbStockRepository.findByCodeAndDayFormat(fiveTgbStock.getCode(),MyUtils.getYesterdayDayFormat());
            if(fiveTgbStock!=null){
                fiveTgbStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
            }else {
                fiveTgbStock.setShowCount(1);
            }
            fiveTgbStockRepository.save(fiveTgbStock);
        }
    }

    private void openFive(){
        List<FiveTgbStock> todayStocks = fiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(FiveTgbStock myStock :todayStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                fiveTgbStockRepository.save(myStock);
            }
        }
        List<FiveTgbStock> myStocks = fiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(FiveTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                fiveTgbStockRepository.save(myStock);
            }
        }
    }
    private void open(){
        openFive();
        List<TgbStock> todayStocks = tgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(TgbStock myStock :todayStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStockRepository.save(myStock);
            }
        }
        List<TgbStock> myStocks = tgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(TgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStockRepository.save(myStock);
            }
        }
    }
    private void closeFive(){
        List<FiveTgbStock> myStocksTomorrow = fiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(FiveTgbStock myStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                fiveTgbStockRepository.save(myStock);
            }
        }
        List<FiveTgbStock> myStocks = fiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(FiveTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                String code = myStock.getCode();
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code, MyUtils.getDayFormat());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    myStock.setOpenCount(xgbStock.getOpenCount());
                }else {
                    myStock.setOpenCount(-1);
                }
                fiveTgbStockRepository.save(myStock);
            }
        }
    }
    private void close(){
        closeFive();
        List<TgbStock> myStocksTomorrow = tgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(TgbStock myStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStockRepository.save(myStock);
            }
        }
        List<TgbStock> myStocks = tgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(TgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                String code = myStock.getCode();
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code, MyUtils.getDayFormat());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    myStock.setOpenCount(xgbStock.getOpenCount());
                }else {
                    myStock.setOpenCount(-1);
                }
                tgbStockRepository.save(myStock);
            }
        }
    }
    //----------------------处理实时获取的数据-----------------------------

    private void choiceCurrent(){
        choiceCurrentFive();
        choiceMe();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay());
        String end = MyUtils.getDayFormat();
        List<MyTotalStock> totalStocks =  currentStockRepository.oneDayInfo(start, end);
        for(MyTotalStock myTotalStock : totalStocks){
            MyTgbStock myTgbStock = new MyTgbStock(myTotalStock.getCode(),myTotalStock.getName());
            myTgbStock.setHotSort(myTotalStock.getTotalCount());
            myTgbStock.setHotValue(myTotalStock.getHotValue());
            myTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
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
    private void choiceCurrentFive(){
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, MyUtils.getCurrentDate()));
        List<MyTotalStock> totalStocks =  currentStockRepository.fiveDayInfo(start, end);
        log.info("current five hot size:"+totalStocks.size());
        for(MyTotalStock myTotalStock : totalStocks){
            MyFiveTgbStock myFiveTgbStock = new MyFiveTgbStock(myTotalStock.getCode(),myTotalStock.getName());
            myFiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            myFiveTgbStock.setHotValue(myTotalStock.getHotValue());
            myFiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
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
            MyFiveTgbStock fiveTgbStock =myFiveTgbStockRepository.findByCodeAndDayFormat(myFiveTgbStock.getCode(),MyUtils.getYesterdayDayFormat());
            if(fiveTgbStock!=null){
                myFiveTgbStock.setShowCount(fiveTgbStock.getShowCount()+1);
            }else {
                myFiveTgbStock.setShowCount(1);
            }
            myFiveTgbStockRepository.save(myFiveTgbStock);
        }
    }
    private void choiceMe(){
        String end = MyUtils.getDayFormat();
        List<FiveTgbStock> hotSortFive = fiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);
        List<MyFiveTgbStock> myTgbStockFive = myFiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);
        Map map = new HashMap();
        for(FiveTgbStock f: hotSortFive){
            map.put(f.getCode(), f);
        }
        for(MyFiveTgbStock f: myTgbStockFive){
            FiveTgbStock five = (FiveTgbStock)map.get(f.getCode());
            if(five!=null){
                meStockRepository.save(five.toMeStock());
            }
        }
    }
    private void openMe(){
        List<MeStock> todayStocks = meStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MeStock myStock :todayStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                meStockRepository.save(myStock);
            }
        }
        List<MeStock> myStocks = meStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MeStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                meStockRepository.save(myStock);
            }
        }
    }
    private void openCurrentFive(){
        List<MyFiveTgbStock> todayStocks = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MyFiveTgbStock myStock :todayStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myFiveTgbStockRepository.save(myStock);
            }
        }
        List<MyFiveTgbStock> myStocks = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MyFiveTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myFiveTgbStockRepository.save(myStock);
            }
        }

    }
    private void openCurrent(){
        openCurrentFive();
        openMe();
        List<MyTgbStock> todayStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(MyTgbStock myStock :todayStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }
        List<MyTgbStock> myStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocks!=null){
            for(MyTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }

    }
    private void closeMe(){
        List<MeStock> myStocksTomorrow = meStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(MeStock myStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                meStockRepository.save(myStock);
            }
        }
        List<MeStock> myStocks = meStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(MeStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                meStockRepository.save(myStock);
            }
        }
    }
    private void closeCurrentFive(){
        List<MyFiveTgbStock> myStocksTomorrow = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(MyFiveTgbStock myStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myFiveTgbStockRepository.save(myStock);
            }
        }
        List<MyFiveTgbStock> myStocks = myFiveTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(MyFiveTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                String code = myStock.getCode();
                List<XGBStock> xgbStocks = xgbStockRepository.findByCodeAndDayFormat(code, MyUtils.getDayFormat());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    XGBStock xgbStock =xgbStocks.get(0);
                    myStock.setOpenCount(xgbStock.getOpenCount());
                }else {
                    myStock.setOpenCount(-1);
                }
                myFiveTgbStockRepository.save(myStock);
            }
        }
    }
    private void closeCurrent(){
        closeCurrentFive();
        closeMe();
        List<MyTgbStock> myStocksTomorrow = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(myStocksTomorrow!=null){
            for(MyTgbStock myStock :myStocksTomorrow){
                String currentPrice = getCurrentPrice(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                myTgbStockRepository.save(myStock);
            }
        }
        List<MyTgbStock> myStocks = myTgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat());
        if(myStocks!=null){
            for(MyTgbStock myStock :myStocks){
                String currentPrice = getCurrentPrice(myStock.getCode());
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

    public void fiveStatistic(){
        String end=MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4,MyUtils.getCurrentDate()));
        List<FiveTgbStock> xgbFiveUpStocks = fiveTgbStockRepository.fiveStatistic(start, end);
        log.info("--->5day count:"+xgbFiveUpStocks.size());
        if(xgbFiveUpStocks.size()>0){
            for (FiveTgbStock xgbFiveUpStock : xgbFiveUpStocks){
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
                fiveTgbStockRepository.save(xgbFiveUpStock);
            }
        }
    }
    public void myFiveStatistic(){
        String end=MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4,MyUtils.getCurrentDate()));
        List<MyFiveTgbStock> xgbFiveUpStocks = myFiveTgbStockRepository.fiveStatistic(start, end);
        log.info("--->5day count:"+xgbFiveUpStocks.size());
        if(xgbFiveUpStocks.size()>0){
            for (MyFiveTgbStock xgbFiveUpStock : xgbFiveUpStocks){
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
                myFiveTgbStockRepository.save(xgbFiveUpStock);
            }
        }
    }
}
