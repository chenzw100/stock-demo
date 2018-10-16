package com.example.stockdemo.controller;

import com.example.stockdemo.service.StockService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping("/taoguba")
    public String taoguba() throws IOException {
        return stockService.choice();
    }
    @RequestMapping("/hello")
    public String hello()  {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int weekday=c.get(Calendar.DAY_OF_WEEK)-1;
        sb.append(DateFormatUtils.format(date, "yy-MM-dd HH:mm:ss")).append(" 星期").append(weekday).append("<br>").append(stockService.chioceResut());

        return sb.toString();
    }
}
