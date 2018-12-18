package com.example.stockdemo.task;

import com.example.stockdemo.service.DownService;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.TgbService;
import com.example.stockdemo.service.UpService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {
    Log log = LogFactory.getLog(ScheduledService.class);

    @Autowired
    TgbService tgbService;

    @Autowired
    private MarketService marketService;
    @Autowired
    private DownService downService;
    @Autowired
    private UpService upService;
    //服务器时间 1-9；7-15，差8小时
    //0 0 9 ? * MON-FRI
    @Scheduled(cron = "40 46 8 ? * MON-FRI")
    //@Scheduled(cron = "0 45 0 ? * MON-FRI")
    public void choice(){
        log.info("==>>exe choice==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        upService.choice();
    }

    @Scheduled(cron = "30 26 9 ? * MON-FRI")
    //@Scheduled(cron = "0 26 1 ? * MON-FRI")
    public void open(){
        log.info("==>>exe open==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        upService.open();
        downService.open();;

    }
    @Scheduled(cron = "0 10 15 ? * MON-FRI")
    //@Scheduled(cron = "0 10 7 ? * MON-FRI")
    public void close(){
        log.info("==>>exe t close==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureClose();
        upService.close();
        downService.close();;
    }

    @Scheduled(cron = "0 35 9 ? * MON-FRI")
    //@Scheduled(cron = "0 35 1 ? * MON-FRI")
    public void topen(){
        log.info("==>>exe t open==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureOpen();
    }
    @Scheduled(cron = "0 45 9,10,13,14 ? * MON-FRI")
    //@Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void temperature(){
        log.info("==>>exe temperature==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.temperatureNormal();
    }
    @Scheduled(cron = "0 5 9 ? * MON-FRI")
    //@Scheduled(cron = "0 5 1 ? * MON-FRI")
    public void clearTemperature(){
        log.info("==>>exe clearTemperature==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        marketService.clearTemperature();
    }

    @Scheduled(cron = "30 41 8 ? * SUN-SAT")
    //@Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void taoguba(){
        log.info("==>>exe choiceLimitUp==>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        upService.taoguba();
    }
    @Scheduled(cron = "0 18,38 6,7,8 ? * MON-FRI")
    //@Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void currentTime(){
        log.info("==>>exe currentTime"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        upService.taogubaCurrent();
    }
    @Scheduled(cron = "0 5,55 2,3 ? * MON-FRI")
    //@Scheduled(cron = "0 45 1,2,5,6 ? * MON-FRI")
    public void testTime(){
        log.info("==>>exe test==start>>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        tgbService.dayTimeStock();
        log.info("==>>exe test==end>>"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
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
