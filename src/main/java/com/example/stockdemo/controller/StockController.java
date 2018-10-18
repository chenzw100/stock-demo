package com.example.stockdemo.controller;

import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.SinaService;
import com.example.stockdemo.service.StockService;
import com.example.stockdemo.service.TgbService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StockController {
    Log log = LogFactory.getLog(StockController.class);
    private static Map<String, MyStock> hot24 = new HashMap();
    @Autowired
    private TgbService tgbService;
    @Autowired
    private SinaService sinaService;
    @Autowired
    private StockService stockService;
    @Autowired
    private MarketService marketService;

    @RequestMapping("/taoguba")
    public String taoguba() throws IOException {
        return stockService.choice();
    }
    @RequestMapping("/hello")
    public String hello()  {
        StringBuilder sb = new StringBuilder();
        Date date = MyUtils.getCurrentDate();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int weekday=c.get(Calendar.DAY_OF_WEEK)-1;
        sb.append(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss")).append(" 星期").append(weekday).append("<br>")
        .append(marketService.temperature()).append("<br>").append(stockService.chioceResut());

        return sb.toString();
    }
}
