package com.example.stockdemo.controller;

import com.example.stockdemo.dao.TemperatureRepository;
import com.example.stockdemo.domain.Temperature;
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

        TreeMap upMap = new TreeMap<>();
        TreeMap downMap = new TreeMap<>();
        for (Temperature t:temperaturesOpen){
            String key = MyUtils.getDayHHFormat(t.getCreated());
            continueValMap.put(key, t.getContinueVal());
            yesterdayShowMap.put(key, MyUtils.getYuanByCent(t.getYesterdayShow()));
            nowTemperatureMap.put(key, t.getNowTemperature());

            upMap.put(key, t.getRaiseUp());
            downMap.put(key, t.getDownUp());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yYesterdayShow", yesterdayShowMap.values());

        resultMap.put("yNowTemperature", nowTemperatureMap.values());
        resultMap.put("yTradeVal", tradeValMap.values());

        resultMap.put("yUp", upMap.values());
        resultMap.put("yDown", downMap.values());
        return resultMap;
    }
    @RequestMapping(value = "/close/{end}", method = RequestMethod.GET)
    public Map close(@PathVariable("end")String end){
        if("1".equals(end)){
            end=MyUtils.getDayFormat();
        }
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(10, endDate));
        List<Temperature> temperaturesOpen=temperatureRepository.close(start, end);
        TreeMap continueValMap = new TreeMap<>();
        TreeMap yesterdayShowMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap tradeValMap = new TreeMap<>();

        TreeMap continueCountMap = new TreeMap<>();
        TreeMap downCountMap = new TreeMap<>();
        for (Temperature t:temperaturesOpen){
            continueValMap.put(t.getDayFormat(), t.getContinueVal());
            yesterdayShowMap.put(t.getDayFormat(), MyUtils.getYuanByCent(t.getYesterdayShow()));
            nowTemperatureMap.put(t.getDayFormat(), t.getNowTemperature());
            tradeValMap.put(t.getDayFormat(), t.getTradeVal());
            continueCountMap.put(t.getDayFormat(), t.getContinueCount());
            downCountMap.put(t.getDayFormat(), t.getStrongDowns());
        }
        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yYesterdayShow", yesterdayShowMap.values());

        resultMap.put("yNowTemperature", nowTemperatureMap.values());
        resultMap.put("yTradeVal", tradeValMap.values());

        resultMap.put("yContinueCount", continueCountMap.values());
        resultMap.put("yDownCount", downCountMap.values());
        return resultMap;
    }



}
