package com.example.stockdemo.task;

import com.example.stockdemo.dao.TgbStockRepository;
import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.service.TgbHotService;
import com.example.stockdemo.utils.ChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyScheduledService {
    Log log = LogFactory.getLog(MyScheduledService.class);
    //选择，开盘，收盘
    @Autowired
    protected TgbHotService tgbHotService;
    @Autowired
    TgbStockRepository tgbStockRepository;
    //服务器时间 1-9；7-15，差8小时
    //0 0 9 ? * MON-FRI
    @Scheduled(cron = "40 48 4 ? * MON-FRI")
    //@Scheduled(cron = "0 45 20 ? * MON-FRI")
    public void choiceWorkDay(){
        log.info("==>>exe choice new Workday"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                tgbHotService.dayTimeStockWorkday();
            }else {
                log.info("==>>exe choice new dayTimeStockWorkday HOLIDAY"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "40 48 4 ? * SUN,SAT")
    //@Scheduled(cron = "0 45 20 ? * MON-FRI")
    public void choiceHoliday(){
        log.info("==>>exe choice new Holiday"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        tgbHotService.dayTimeStockHoliday();

    }
    @Scheduled(cron = "30 27 9 ? * MON-FRI")
    //@Scheduled(cron = "0 26 1 ? * MON-FRI")
    public void open(){
        log.info("==>>exe open new "+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                tgbHotService.open();
            }else {
                log.info("==>>exe open  HOLIDAY"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Scheduled(cron = "0 20 15 ? * MON-FRI")
    //@Scheduled(cron = "0 10 7 ? * MON-FRI")
    public void close(){
        log.info("==>>exe t close new "+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                tgbHotService.close();
                List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByHotSort(MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
                MailSendUtil.sendMail(hotSort.toString());
            }else {
                log.info("==>>exe close new  HOLIDAY"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0 10 8,9 ? * MON-FRI")
    //@Scheduled(cron = "0 15,31,49 23,0 ? * MON-FRI")
    public void currentTime(){
        log.info("==>>exe new currentTime" + DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                tgbHotService.taogubaCurrent();
            }else {
                log.info("==>>exe  currentTime2 HOLIDAY"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Scheduled(cron = "0 25,35,45,55 8 ? * MON-FRI")
    //@Scheduled(cron = "0 15,31,49 23,0 ? * MON-FRI")
    public void currentTime2(){
        log.info("==>>exe new currentTime"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                tgbHotService.taogubaCurrent();
            }else {
                log.info("==>>exe  currentTime2 HOLIDAY"+ DateFormatUtils.format(MyUtils.getCurrentDate(), "yyMMdd HH:mm:ss"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   *
     * 一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。按顺序依次为：
     *
     * 1 秒（0~59）
     * 2 分钟（0~59）
     * 3 小时（0~23）
     * 4 天（0~31）
     * 5 月（0~11）
     * 6 星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     * 7 年份（1970－2099）
     **/

}
