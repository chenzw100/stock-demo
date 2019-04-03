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



}
