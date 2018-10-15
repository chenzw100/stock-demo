package com.example.stockdemo.task;

import com.example.stockdemo.service.StockService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledService {
    Log log = LogFactory.getLog(ScheduledService.class);
    @Autowired
    private StockService stockService;
    //0 0 9 ? * MON-FRI
    @Scheduled(cron = "0 0 8,9 ? * MON-FRI")
    public void scheduled(){
        log.info("=====>>>>>use cron");
        try {
            stockService.taoguba();
            //MailSendUtil.sendMail(content);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /*@Scheduled(fixedRate = 5000)
    public void scheduled1() {
        System.out.printf("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
    }
    @Scheduled(fixedDelay = 5000)
    public void scheduled2() {
        System.out.printf("=====>>>>>fixedDelay{}",System.currentTimeMillis());
    }*/
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
     */

}
