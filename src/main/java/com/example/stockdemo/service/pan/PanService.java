package com.example.stockdemo.service.pan;

import com.example.stockdemo.service.tgb.TgbDealDataService;
import com.example.stockdemo.service.tgb.TgbService;
import com.example.stockdemo.service.xgb.XgbDealDataService;
import com.example.stockdemo.utils.ChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class PanService {
    Log log = LogFactory.getLog(PanService.class);
    private static final String openCron = "50 25 9 ? * MON-FRI";
    private static final String closeCron ="0 6 15 ? * MON-FRI";
    private static final String choiceMy="42 1 9 ? * MON-FRI";
    private static final String currentTimeCron="1 59 0/2 ? * MON-FRI";
    private static final String temperatureCron="0 45 9,10,11,13,14 ? * MON-FRI";
    private static final String temperatureOpenCron="30 33 9 ? * MON-FRI";
    @Autowired
    TgbDealDataService tgbDealDataService;
    @Autowired
    TgbService tgbService;
    @Autowired
    XgbDealDataService xgbDealDataService;
    //盘前处理数据 9:03点获取
    @Scheduled(cron = choiceMy)
    public void preOpen(){
        //获取数据
        if(isWorkday()){
            log.info("preOne-ready data");
            tgbService.dayDate();
            tgbDealDataService.prePan();
        }
    }
    //9:26处理数据
    @Scheduled(cron = openCron)
    public void openPan(){
        if(isWorkday()){
            log.info("openPan-ready data");
            tgbDealDataService.openPan();
            xgbDealDataService.openPan();
        }
    }
    //9:33处理数据
    @Scheduled(cron = temperatureOpenCron)
    public void openFivePan(){
        if(isWorkday()) {
            log.info("openFivePan-ready data");
            xgbDealDataService.openFivePan();
        }
    }
    //15:08处理数据
    @Scheduled(cron = closeCron)
    public void closePan(){
        if(isWorkday()) {
            log.info("closePan-ready data");
            tgbDealDataService.closePan();
            xgbDealDataService.closePan();
        }
    }
    //盘中每小时处理数据
    @Scheduled(cron = temperatureCron)
    public void currentPan(){
        if(isWorkday()) {
            log.info("currentPan-ready data");
            xgbDealDataService.current();
        }
    }
    //每2小时收集数据
    @Scheduled(cron = currentTimeCron)
    public void allDay(){
        if(isWorkday()) {
            log.info("currentDate-ready data");
            tgbService.currentDate();
        }
    }

    public boolean isWorkday(){
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
