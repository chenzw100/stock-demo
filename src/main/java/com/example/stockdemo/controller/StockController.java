package com.example.stockdemo.controller;

import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.service.MarketService;
import com.example.stockdemo.service.TgbHotService;
import com.example.stockdemo.service.UpService;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    RestTemplate restTemplate;
    @Autowired
    private TgbHotService tgbHotService;
    @RequestMapping("/f/{format}")
    String f(@PathVariable("format")String format) {
        List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByStockType(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(format);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        return format+":<br>"+hotSort+":<br>"+temperatures+":<br>"+downStocks;
    }
    @RequestMapping("/s/{format}")
    String s(@PathVariable("format")String format) {
        List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByHotSort(format);
        List<TgbStock> hotSeven = tgbStockRepository.findByDayFormatOrderByHotSevenDesc(format);
        List<TgbStock> openBidRate = tgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(format);
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        return format+":<br>"+hotSort+":<br>"+temperatures+":<br>"+downStocks+":<br>"+format+":<br>"+openBidRate+":<br>"+format+":<br>"+hotSeven;
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
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
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
