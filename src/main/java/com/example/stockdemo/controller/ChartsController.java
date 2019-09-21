package com.example.stockdemo.controller;

import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.service.DownService;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.TgbHotService;
import com.example.stockdemo.service.pan.PanService;
import com.example.stockdemo.utils.ChineseWorkDay;
import com.example.stockdemo.utils.MyChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ChartsController {
    @Autowired
    TemperatureRepository temperatureRepository;
    @Autowired
    FiveTgbStockRepository fiveTgbStockRepository;
    @Autowired
    MyFiveTgbStockRepository myFiveTgbStockRepository;
    @Autowired
    MeStockRepository meStockRepository;
    @Autowired
    DownStockAverageRepository downStockAverageRepository;
    @Autowired
    DownStockRepository downStockRepository;
    @Autowired
    XgbStockRepository xgbStockRepository;
    @Autowired
    SpaceHeightRepository spaceHeightRepository;
    @Autowired
    DownService downService;
    @Autowired
    MarketService marketService;
    @Autowired
    protected TgbHotService tgbHotService;
    @Autowired
    PanService panService;


    private static String PRE_END="";
    @RequestMapping("/t/{end}")
    List<Temperature> m(@PathVariable("end")String end) {
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
       // List<Temperature> temperaturesOpen=temperatureRepository.open(start,end);
        List<Temperature> temperaturesClose=temperatureRepository.close(start, end);
        return temperaturesClose;
    }
    @RequestMapping(value = "/index/{end}", method = RequestMethod.GET)
    public Map index(@PathVariable("end")String end){
        if("1".equals(end)){
            end=MyUtils.getDayFormat();
        }
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(10, endDate));
        List<Temperature> temperaturesOpen=temperatureRepository.open(start,end);
        List<Temperature> temperaturesClose=temperatureRepository.close(start, end);
        List<Temperature> temperatures=temperatureRepository.normal(start, end);
        TreeMap xyMap = new TreeMap<>();//线状图数据
        TreeMap xy2Map = new TreeMap<>();
        TreeMap xy3Map = new TreeMap<>();

        for (Temperature t:temperaturesClose){
            xyMap.put(t.getDayFormat(), t.getContinueVal());
            xy2Map.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getYesterdayShow()));
            xy3Map.put(t.getDayFormat(), t.getNowTemperature());

        }
        TreeMap xy4Map = new TreeMap<>();
        TreeMap xy5Map = new TreeMap<>();
        TreeMap xy6Map = new TreeMap<>();
        for (Temperature t:temperaturesOpen){
            xy4Map.put(t.getDayFormat(), t.getContinueVal());
            xy5Map.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getYesterdayShow()));
            xy6Map.put(t.getDayFormat(), t.getNowTemperature());
        }
        TreeMap xy7Map = new TreeMap<>();
        TreeMap xy8Map = new TreeMap<>();
        TreeMap xy9Map = new TreeMap<>();
        for (Temperature t:temperatures){
            String key = MyUtils.getDayHHFormat(t.getCreated());
            xy7Map.put(key, t.getContinueVal());
            xy8Map.put(key, MyUtils.getYuanByCent(t.getYesterdayShow()));
            xy9Map.put(key, t.getNowTemperature());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", xyMap.keySet());
        resultMap.put("y", xyMap.values());
        resultMap.put("y2", xy2Map.values());
        resultMap.put("y3", xy3Map.values());
        resultMap.put("y4", xy4Map.values());
        resultMap.put("y5", xy5Map.values());
        resultMap.put("y6", xy6Map.values());

        resultMap.put("x2", xy7Map.keySet());
        resultMap.put("y7", xy7Map.values());
        resultMap.put("y8", xy8Map.values());
        resultMap.put("y9", xy9Map.values());
        return resultMap;
    }
    @RequestMapping(value = "/open/{end}", method = RequestMethod.GET)
    public Map open(@PathVariable("end")String end){
        if("1".equals(end)){
            end=MyUtils.getDayFormat();
        }
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(10, endDate));
        List<Temperature> temperaturesOpen=temperatureRepository.open(start,end);
        TreeMap continueValMap = new TreeMap<>();
        TreeMap yesterdayShowMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap tradeValMap = new TreeMap<>();
        for (Temperature t:temperaturesOpen){
            continueValMap.put(t.getDayFormat(), t.getContinueVal());
            yesterdayShowMap.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getYesterdayShow()));
            nowTemperatureMap.put(t.getDayFormat(), t.getNowTemperature());
            tradeValMap.put(t.getDayFormat(), t.getTradeVal());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yYesterdayShow", yesterdayShowMap.values());

        resultMap.put("yNowTemperature", nowTemperatureMap.values());
        resultMap.put("yTradeVal", tradeValMap.values());
        return resultMap;
    }

    @RequestMapping(value = "/current/{end}", method = RequestMethod.GET)
    public Map current(@PathVariable("end")String end){
        if("1".equals(end)){
            end=MyUtils.getDayFormat();
        }
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(10, endDate));
        List<Temperature> temperaturesOpen=temperatureRepository.current(start, end);
        TreeMap continueValMap = new TreeMap<>();
        TreeMap yesterdayShowMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap tradeValMap = new TreeMap<>();


        for (Temperature t:temperaturesOpen){
            String key = MyUtils.getDayHHFormat(t.getCreated());
            continueValMap.put(key, t.getContinueVal());
            yesterdayShowMap.put(key, MyUtils.getYuanByCent(t.getYesterdayShow()));
            nowTemperatureMap.put(key, t.getNowTemperature());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yYesterdayShow", yesterdayShowMap.values());

        resultMap.put("yNowTemperature", nowTemperatureMap.values());
        resultMap.put("yTradeVal", tradeValMap.values());



        return resultMap;
    }
    @RequestMapping(value = "/close/{end}", method = RequestMethod.GET)
    public Map close(@PathVariable("end")String end){
        String queryEnd = end;
        if("1".equals(end)){
            queryEnd=MyUtils.getDayFormat();
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(12, endDate));
        List<Temperature> temperaturesClose=temperatureRepository.close(start, queryEnd);
        List<DownStockAverage> downStockAverages = downStockAverageRepository.close(start, queryEnd);
        TreeMap continueValMap = new TreeMap<>();
        TreeMap yesterdayShowMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap tradeValMap = new TreeMap<>();

        TreeMap continueCountMap = new TreeMap<>();
        TreeMap downCountMap = new TreeMap<>();
        TreeMap upMap = new TreeMap<>();
        TreeMap downMap = new TreeMap<>();
        TreeMap limitUpMap = new TreeMap<>();
        TreeMap limitDownMap = new TreeMap<>();
        TreeMap brokenMap = new TreeMap<>();

        TreeMap downAverageTodayOpen = new TreeMap<>();
        TreeMap downAverageTodayClose = new TreeMap<>();
        TreeMap downAverageTomorrowOpen = new TreeMap<>();
        TreeMap downAverageTomorrowClose = new TreeMap<>();

        for (Temperature t:temperaturesClose){
            continueValMap.put(t.getDayFormat(), t.getContinueVal());
            yesterdayShowMap.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getYesterdayShow()));
            nowTemperatureMap.put(t.getDayFormat(), t.getNowTemperature());
            tradeValMap.put(t.getDayFormat(), t.getTradeVal());
            continueCountMap.put(t.getDayFormat(), t.getContinueCount());
            downCountMap.put(t.getDayFormat(), t.getStrongDowns());
            upMap.put(t.getDayFormat(), t.getRaiseUp());
            downMap.put(t.getDayFormat(), t.getDownUp());
            limitUpMap.put(t.getDayFormat(), t.getLimitUp());
            limitDownMap.put(t.getDayFormat(), t.getLimitDown());
            brokenMap.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getBrokenRatio()));
        }
        for (DownStockAverage average :downStockAverages){
            downAverageTodayOpen.put(average.getDayFormat(),average.getTodayOpenRate());
            downAverageTodayClose.put(average.getDayFormat(),average.getTodayCloseRate());
            downAverageTomorrowOpen.put(average.getDayFormat(),average.getTomorrowOpenRate());
            downAverageTomorrowClose.put(average.getDayFormat(),average.getTomorrowCloseRate());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yYesterdayShow", yesterdayShowMap.values());

        resultMap.put("yNowTemperature", nowTemperatureMap.values());
        resultMap.put("yTradeVal", tradeValMap.values());

        resultMap.put("yContinueCount", continueCountMap.values());
        resultMap.put("yDownCount", downCountMap.values());

        resultMap.put("yUp", upMap.values());
        resultMap.put("yDown", downMap.values());

        resultMap.put("yLimitUp", limitUpMap.values());
        resultMap.put("yLimitDown", limitDownMap.values());
        resultMap.put("yBroken", brokenMap.values());

        resultMap.put("yAverageTodayOpen", downAverageTodayOpen.values());
        resultMap.put("yAverageTodayClose", downAverageTodayClose.values());

        resultMap.put("yAverageTomorrowOpen", downAverageTomorrowOpen.values());
        resultMap.put("yAverageTomorrowClose", downAverageTomorrowClose.values());

        List<MeStock> hotSortFive = meStockRepository.findByDayFormatOrderByOpenBidRate(queryEnd);
        resultMap.put("hot",hotSortFive);

        TreeMap firstContinue = new TreeMap<>();
        TreeMap secondContinue = new TreeMap<>();
        List<SpaceHeight> spaceHeights = spaceHeightRepository.close(start, queryEnd);
        for(SpaceHeight sh:spaceHeights){
            firstContinue.put(sh.getDayFormat(),sh.getFirstContinue());
            secondContinue.put(sh.getDayFormat(),sh.getSecondContinue());
        }
        resultMap.put("firstContinue", firstContinue.values());
        resultMap.put("secondContinue", secondContinue.values());

        if(temperaturesClose.size()==0){
            close();
        }
        return resultMap;
    }
    public boolean isWorkday(){
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @RequestMapping(value = "/risk/{end}", method = RequestMethod.GET)
     public Map risk(@PathVariable("end")String end){
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd=MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(12, endDate));
        List<Temperature> temperaturesClose=temperatureRepository.close(start, queryEnd);

        TreeMap continueValMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap firstContinue = new TreeMap<>();

        TreeMap continueCountMap = new TreeMap<>();
        TreeMap downCountMap = new TreeMap<>();

        TreeMap limitUpMap = new TreeMap<>();
        TreeMap limitDownMap = new TreeMap<>();

        for (Temperature t:temperaturesClose){
            continueValMap.put(t.getDayFormat(), t.getContinueVal());
            nowTemperatureMap.put(t.getDayFormat(), t.getNowTemperature());
            continueCountMap.put(t.getDayFormat(), t.getContinueCount());
            downCountMap.put(t.getDayFormat(), t.getStrongDowns());
            limitUpMap.put(t.getDayFormat(), t.getLimitUp());
            limitDownMap.put(t.getDayFormat(), t.getLimitDown());
        }

        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yNowTemperature", nowTemperatureMap.values());

        resultMap.put("yContinueCount", continueCountMap.values());
        resultMap.put("yLimitUp", limitUpMap.values());

        resultMap.put("yDownCount", downCountMap.values());
        resultMap.put("yLimitDown", limitDownMap.values());


        List<SpaceHeight> spaceHeights = spaceHeightRepository.close(start, queryEnd);
        for(SpaceHeight sh:spaceHeights){
            firstContinue.put(sh.getDayFormat(),sh.getFirstContinue());
        }
        resultMap.put("firstContinue", firstContinue.values());

        return resultMap;
    }
    void h(String end) {
        List<XGBStock> hs=xgbStockRepository.findByDayFormatOrderByContinueBoardCountDesc(end);
        SpaceHeight spaceHeight = new SpaceHeight();
        spaceHeight.setCreated(new Date());
        if(hs!=null && hs.size()>0){
            XGBStock hstock = hs.get(0);
            spaceHeight.setDayFormat(end);
            spaceHeight.setFirstCode(hstock.getCode());
            spaceHeight.setFirstName(hstock.getName());
            spaceHeight.setFirstOpen(hstock.getOpenCount());
            spaceHeight.setFirstContinue(hstock.getContinueBoardCount());
            spaceHeight.setFirstPlate(hstock.getPlateName());

        }
        if(hs!=null && hs.size()>1){
            XGBStock hstock = hs.get(1);
            spaceHeight.setSecondCode(hstock.getCode());
            spaceHeight.setSecondName(hstock.getName());
            spaceHeight.setSecondOpen(hstock.getOpenCount());
            spaceHeight.setSecondContinue(hstock.getContinueBoardCount());
            spaceHeight.setSecondPlate(hstock.getPlateName());
        }
        spaceHeightRepository.save(spaceHeight);
    }

    void s1(String end) {
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
    void s(String end){
        List<DownStock> downStocks = downStockRepository.findByDayFormatOrderByOpenBidRate(end);
        int openAverage = 0;
        int openAverageClose = 0;
        int openTOpen = 0;
        int openTClose = 0;
        int dSize = downStocks.size();
        for (DownStock downStock:downStocks){
            openAverage=openAverage+downStock.getOpenBidRate();
            openAverageClose = openAverageClose + MyUtils.getCentByYuanStr(downStock.getTodayCloseRate());
            openTOpen=openTOpen+MyUtils.getCentByYuanStr(downStock.getTomorrowOpenRate());
            openTClose = openTClose + MyUtils.getCentByYuanStr(downStock.getTomorrowCloseRate());
        }
        if(openAverage!=0){
            DownStockAverage downStockAverage = queryDayFormat(end);
            downStockAverage.setTodayOpenRate(MyUtils.getAverageRateCent(openAverage,dSize).intValue());
            downStockAverage.setTodayCloseRate(MyUtils.getAverageRateCent(openAverageClose, dSize).intValue());
            downStockAverage.setTomorrowOpenRate(MyUtils.getAverageRateCent(openTOpen, dSize).intValue());
            downStockAverage.setTomorrowCloseRate(MyUtils.getAverageRateCent(openTClose, dSize).intValue());
            saveDayFormat(downStockAverage);
        }
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

    private void close(){
        panService.closePan();
    }


}
