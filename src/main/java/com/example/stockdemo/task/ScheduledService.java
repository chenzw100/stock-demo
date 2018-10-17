package com.example.stockdemo.task;

import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.StockService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class ScheduledService {
    Log log = LogFactory.getLog(ScheduledService.class);
    @Autowired
    private StockService stockService;
    @Autowired
    private MarketService marketService;
    //服务器时间 1-9；7-15，差8小时
    //0 0 9 ? * MON-FRI
    @Scheduled(cron = "0 45 0 ? * MON-FRI")
    public void choice(){
        log.info("==>>exe choice"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        try {
            stockService.choice();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Scheduled(cron = "0 26 1 ? * MON-FRI")
    public void open(){
        log.info("==>>exe open"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        stockService.open();

    }
    @Scheduled(cron = "0 10 7 ? * MON-FRI")
    public void close(){
        log.info("==>>exe close"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperature();
        stockService.close();
    }
    @Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void temperature(){
        log.info("==>>exe temperature"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperature();
    }
    @Scheduled(cron = "0 5 1 ? * MON-FRI")
    public void clearTemperature(){
        log.info("==>>exe clearTemperature"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.clearTemperature();
    }

    /**
     * 一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。按顺序依次为：
     *
     * 1 秒（0~59）
     * 2 分钟（0~59）
     * 3 小时（0~23）
     * 4 天（0~31）
     * 5 月（0~11）
     * 6 星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     * 7 年份（1970－2099）
     *
     */

}
