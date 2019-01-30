package com.example.stockdemo.controller;

import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.TgbHotService;
import com.example.stockdemo.service.UpService;
import com.example.stockdemo.utils.MyChineseWorkDay;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class StockController {
    @Autowired
    private UpService upService;
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
    @Autowired
    TgbStockRepository tgbStockRepository;
    @Autowired
    MyTgbStockRepository myTgbStockRepository;
    @Autowired
    FiveTgbStockRepository fiveTgbStockRepository;
    @Autowired
    MyFiveTgbStockRepository myFiveTgbStockRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private TgbHotService tgbHotService;

    @RequestMapping("/e/{end}")
    String e(@PathVariable("end")String end) {
        String desc ="坚持模式！！！<br>【跌停数,炸板，强势股计提,焦点股不涨停计提】<br>查询日期";
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4,endDate));
        List<MyTotalStock> totalStocks =tgbStockRepository.stockInfo(start, end);
        List<MyTotalStockImpl> totalStocks1 = new ArrayList<>();
        for(MyTotalStock totalStock:totalStocks){
            MyTotalStockImpl totalStock1= new MyTotalStockImpl(totalStock);
            totalStocks1.add(totalStock1);
        }
        List<MyTotalStock> totalStocksCurrent =currentStockRepository.fiveDayInfo(start, end);
        List<MyTotalStockImpl> myTotalStocks = new ArrayList<>();
        for(MyTotalStock totalStock:totalStocksCurrent){
            MyTotalStockImpl totalStock1= new MyTotalStockImpl(totalStock);
            myTotalStocks.add(totalStock1);
        }
        List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(end);
        List<MyTgbStock> myTgbStockList = myTgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(end);

        List<FiveTgbStock> hotSortFive = fiveTgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(end);
        List<MyFiveTgbStock> myTgbStockFive = myFiveTgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(end);

        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(end);
        List<DownStock> downBeforeStocks =downStockRepository.findByPreFormatOrderByOpenBidRateDesc(end);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(end);
        return desc+start+"-"+end+"昨日:<br>"+downStocks+"热议:<br>"+totalStocks1+"竞价:<br>"+hotSort+"5日竞价:<br>"+hotSortFive+"实时:<br>"+myTotalStocks+"实时竞价:<br>"+myTgbStockList+"5日实时竞价:<br>"+myTgbStockFive+":<br>"+temperatures+end+"当日:<br>"+downBeforeStocks;
    }
    @RequestMapping("/m/{end}")
    String m(@PathVariable("end")String end) {
        String desc ="查询20190124之后的数据，坚持模式！！！<br>【跌停数,炸板，强势股计提,焦点股不涨停计提】<br>查询日期";
        Date endDate =  MyUtils.getFormatDate(end);
        String yesterday =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(1,endDate));
        List<Temperature> yesterdays = temperatureRepository.findByDayFormatAndType(yesterday,NumberEnum.TemperatureType.CLOSE.getCode());
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<FiveTgbStock> hotSortFive = fiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);
        List<MyFiveTgbStock> myTgbStockFive = myFiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<TgbStock> tgbHots = tgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(end);
        List<DownStock> downBeforeStocks =downStockRepository.findByPreFormatOrderByOpenBidRateDesc(end);

        return desc+end+"昨日情况:<br>"+downStocks+"<br>"+yesterdays+"<br>股吧竞价:<br>"+hotSortFive+"end"+end+"<br>我的竞价:<br>"+myTgbStockFive+"<br>股吧热门:<br>"+tgbHots+":<br>"+temperatures+end+"当日:<br>"+downBeforeStocks;
    }
    @RequestMapping("/s/{format}")
    String s(@PathVariable("format")String format) {
        List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByHotSort(format);
        List<TgbStock> hotSeven = tgbStockRepository.findByDayFormatOrderByHotSevenDesc(format);
        List<TgbStock> openBidRate = tgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(format);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(format);
        return format+":竞价<br>"+openBidRate+":温度<br>"+temperatures+":亏钱<br>"+downStocks+":<br>"+format+":热排<br>"+hotSort+":<br>"+format+":七日<br>"+hotSeven;
    }

    @RequestMapping("zt")
    String zt() {
        tgbHotService.closeLimitUp();
        return "success";
    }

    @RequestMapping("xz")
    String xz() {
        tgbHotService.dayTimeStockWorkday();
        return "success";
    }

    @RequestMapping("/bid/{code}")
    public String bid(@PathVariable("code")String code)  {
        if("1".equals(code)){
            return "success";
        }
        if(code.indexOf("6")==0){
            code = "sh"+code;
        }else {
            code = "sz"+code;
        }
        SinaStock sinaStock = getSinaStock(code);
        if(sinaStock ==null){
            return "fail";
        }
        MyStock myStock = new MyStock(code,sinaStock.getName());
        myStock.setCreated(MyUtils.getCurrentDate());
        myStock.setStockType(NumberEnum.StockType.BID.getCode());
        myStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getYesterdayClosingPrice()));
        myStock.setContinuous("0");
        myStock.setOpenCount(-1);
        myStock.setHotSort(-1);
        myStock.setOneFlag(-1);
        myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(sinaStock.getOpeningPrice()));
        myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
        myStockRepository.save(myStock);

        return myStock.toString();
    }
    private SinaStock getSinaStock(String code) {
        String url ="https://hq.sinajs.cn/list="+code;
        Object response =  restTemplate.getForObject(url, String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<3){
            //log.error(code + ":err=" + str);
            return null;
        }
        int length = stockObj[0].length();
        String strName = stockObj[0].substring(stockObj[0].indexOf("=")+2,length);
        return new SinaStock(code,strName,stockObj[1],stockObj[2],stockObj[3]);
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
        List<MyStock> openBidRate = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        List<MyStock> stockType = myStockRepository.findByDayFormatOrderByStockType(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(format);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(format);
        //List<StrongStocksDown> strongStocksDowns =strongStocksDownRepository.findByDayFormat(format);
        return format+":<br>"+openBidRate+":<br>"+temperatures+":<br>"+downStocks+":<br>"+stockType;
    }
    @RequestMapping("z")
    String z() {
        upService.closeLimitUp();
        return "success";
    }

    @RequestMapping("down")
    String down() {
        marketService.boomStock();
        marketService.multiStock();
        return "success";
    }
    @RequestMapping("t")
    String t() {
        MailSendUtil.sendMail("test");
        return "test-success";
    }



    public static void main(String[] args) {
        System.out.println(11);
        String code = "600555";
        String code1 = "600655";
        String code2 = "000655";
        if(code.indexOf("6")==0){
            System.out.println(code);
        }
        if(code1.indexOf("6")==0){
            System.out.println(code1);
        }
        if(code2.indexOf("6")==0){
            System.out.println(code2);
        }
    }
}
