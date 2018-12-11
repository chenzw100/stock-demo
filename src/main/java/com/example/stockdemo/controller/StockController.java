package com.example.stockdemo.controller;

import com.example.stockdemo.dao.DownStockRepository;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.dao.StrongStocksDownRepository;
import com.example.stockdemo.dao.TemperatureRepository;
import com.example.stockdemo.domain.DownStock;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.StrongStocksDown;
import com.example.stockdemo.domain.Temperature;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.MarketStockService;
import com.example.stockdemo.service.TgbService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class StockController {
    @Autowired
    private TgbService tgbService;

    @Autowired
    private MarketStockService tgbMarketStockService;
    @Autowired
    private MarketService marketService;
    @Autowired
    MyStockRepository myStockRepository;
    @Autowired
    StrongStocksDownRepository strongStocksDownRepository;
    @Autowired
    TemperatureRepository temperatureRepository;
    @Autowired
    DownStockRepository downStockRepository;

    @RequestMapping("/hello")
    public String hello()  {
        StringBuilder sb = new StringBuilder();
        Date date = MyUtils.getCurrentDate();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int weekday=c.get(Calendar.DAY_OF_WEEK)-1;
        Map<String, MyStock> tgbHot24 =tgbService.getHop24Stock();

        sb.append(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss")).append(" 星期").append(weekday).append("<br>");
        List<Temperature> temperatures = temperatureRepository.findByDayFormat(DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd"));
        sb.append(temperatures).append("<br>");
        sb.append("淘县实时热搜:<br>");
        for (String code:tgbHot24.keySet()){
            MyStock myStock = tgbHot24.get(code);
            sb.append(myStock.getName()).append("<br>");
        }

        return sb.toString();
    }

    @RequestMapping("/format/{format}")
    String format(@PathVariable("format")String format) {
        return formatData(format);
    }
    @RequestMapping("m")
    String m() {
        String today = DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd");
        return formatData(today);
    }
    String formatData(String format) {
        List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(format);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        //List<StrongStocksDown> strongStocksDowns =strongStocksDownRepository.findByDayFormat(format);
        return format+":<br>"+myStocks+":<br>"+temperatures+":<br>"+downStocks;
    }
    @RequestMapping("z")
    String z() {
        tgbMarketStockService.closeLimitUp();
        return "success";
    }
    @RequestMapping("c")
    String c() {
        Map map = tgbMarketStockService.getHopStock();
        if(map== null || map.size()==0){
            tgbService.currentTimeStock();
            map= tgbService.getCurrentTime();
        }
        return "<span color='red'>实时</span>:<br>"+map;
    }
    @RequestMapping("down")
    String down() {
        marketService.boomStock();
        marketService.multiStock();
        return "success";
    }
    @RequestMapping("t")
    String t() {
        return "test-success";
    }
}
