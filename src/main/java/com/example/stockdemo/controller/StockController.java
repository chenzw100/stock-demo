package com.example.stockdemo.controller;

import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.service.MarketStockService;
import com.example.stockdemo.service.TgbService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class StockController {
    @Autowired
    private TgbService tgbService;
    @Autowired
    private MarketStockService tgbMarketStockService;
    @Autowired
    MyStockRepository myStockRepository;
    @RequestMapping("/choice")
    public String choice(){
        return tgbMarketStockService.choiceYesterday();
    }
    @RequestMapping("/open")
    public String open() {
        return tgbMarketStockService.open();
    }
    @RequestMapping("/close")
    public String close() {
        return tgbMarketStockService.close();
    }
    @RequestMapping("/hello")
    public String hello()  {
        StringBuilder sb = new StringBuilder();
        Date date = MyUtils.getCurrentDate();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int weekday=c.get(Calendar.DAY_OF_WEEK)-1;
        Map<String, MyStock> tgbHot24 =tgbService.getHop24Stock();

        sb.append(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss")).append(" 星期").append(weekday).append("<br>");
       // .append(marketService.temperature()).append("<br>");
        sb.append("淘县实时热搜:<br>");
        for (String code:tgbHot24.keySet()){
            MyStock myStock = tgbHot24.get(code);
            sb.append(myStock.getName()).append("<br>");
            myStockRepository.save(myStock);
        }
       /* Map<String, MyStock> sinaHot24 =sinaService.getHop24Stock();
        sb.append("微博实时热搜:<br>");
        for (String code:sinaHot24.keySet()){
            MyStock myStock = sinaHot24.get(code);
            sb.append(myStock.getName()).append("<br>");
        }*/
        return sb.toString();
    }

    @RequestMapping("/code/{code}")
    String code(@PathVariable("code")String code) {
        List<MyStock> myStocks = myStockRepository.findByCode(code);
        return code+":<br>"+myStocks;
    }
    @RequestMapping("/format/{format}")
    String format(@PathVariable("format")String format) {
        List<MyStock> myStocks = myStockRepository.findByDayFormat(format);
        return format+":<br>"+myStocks;
    }
}
