package com.example.stockdemo.task;

import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.MarketStockService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduledService {
    Log log = LogFactory.getLog(MyScheduledService.class);

    @Autowired
    private MarketService marketService;
    @Autowired
    private MarketStockService tgbMarketStockService;
    //服务器时间 1-9；7-15，差8小时
    //0 0 9 ? * MON-FRI
    //@Scheduled(cron = "0 49 8 ? * MON-FRI")
    @Scheduled(cron = "0 45 0 ? * MON-FRI")
    public void choice(){
        log.info("==>>exe choice"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        tgbMarketStockService.choiceYesterday();

    }
    //@Scheduled(cron = "30 26 9 ? * MON-FRI")
    @Scheduled(cron = "0 26 1 ? * MON-FRI")
    public void open(){
        log.info("==>>exe open"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        tgbMarketStockService.open();

    }
    //@Scheduled(cron = "0 10 15 ? * MON-FRI")
    @Scheduled(cron = "0 10 7 ? * MON-FRI")
    public void close(){
        log.info("==>>exe t close"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureClose();
        tgbMarketStockService.close();
    }
    //@Scheduled(cron = "0 35 9 ? * MON-FRI")
    @Scheduled(cron = "0 35 1 ? * MON-FRI")
    public void topen(){
        log.info("==>>exe t open"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureOpen();
    }
    //@Scheduled(cron = "0 45 9,10,13,14 ? * MON-FRI")
    @Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void temperature(){
        log.info("==>>exe temperature"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureNormal();
    }
    //@Scheduled(cron = "0 5 9 ? * MON-FRI")
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
