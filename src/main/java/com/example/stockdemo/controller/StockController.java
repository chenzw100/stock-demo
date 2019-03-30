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
    @Autowired
    private XgbStockRepository xgbStockRepository;

    @RequestMapping("/e/{end}")
    String e(@PathVariable("end")String end) {
        String desc ="坚持模式！！！<br>【跌停数,炸板，强势股计提,焦点股不涨停计提】<br>查询日期";
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(4,endDate));
        List<MyTotalStock> totalStocks =tgbStockRepository.limit4(start, end);
        List<MyTotalStockImpl> totalStocks1 = new ArrayList<>();
        for(MyTotalStock totalStock:totalStocks){
            MyTotalStockImpl totalStock1= new MyTotalStockImpl(totalStock);
            totalStocks1.add(totalStock1);
        }
        List<MyTotalStock> totalStocksCurrent =currentStockRepository.limit4(start, end);
        List<MyTotalStockImpl> myTotalStocks = new ArrayList<>();
        for(MyTotalStock totalStock:totalStocksCurrent){
            MyTotalStockImpl totalStock1= new MyTotalStockImpl(totalStock);
            myTotalStocks.add(totalStock1);
        }

        return desc+start+"-"+end+"股吧热议:<br>"+totalStocks1+"自助热议:<br>"+myTotalStocks;
    }
    @RequestMapping("/m/{end}")
    String m(@PathVariable("end")String end) {
        String desc ="查询20190124之后的数据，坚持模式！！【聚焦强势聚焦主流聚焦前排】！<br>【跌停数,炸板，强势股计提,焦点股不涨停计提】<br>【热闹之后，强势股开盘大跌；开一字封单跟不上；大牛市沸点到冰点一根稻草20190308，一天时间逆转那么多】<br>" +
                "【有利空的还是尽量规避!如：20190317之002750龙津药业】<br>【市场疯狂调整更疯狂，请查看20190226,20190308,20190313,20190321,20190328】<br><br>查询日期";
        Date endDate =  MyUtils.getFormatDate(end);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5,endDate));
        endDate =  MyUtils.getFormatDate(end);
        String yesterday =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(1,endDate));
        List<XGBStock> xs=xgbStockRepository.findByDayFormatAndContinueBoardCountGreaterThan(yesterday,1);
        //List<Temperature> yesterdays = temperatureRepository.findByDayFormatAndType(yesterday,NumberEnum.TemperatureType.CLOSE.getCode());
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<FiveTgbStock> hotSortFive = fiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);
        List<MyFiveTgbStock> myTgbStockFive = myFiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<TgbStock> tgbHots = tgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderById(end);
        List<DownStock> downBeforeStocks =downStockRepository.findByPreFormatOrderByOpenBidRateDesc(end);

        List<Temperature> temperaturesOpen=temperatureRepository.open(start,end);
        List<Temperature> temperaturesClose=temperatureRepository.close(start,end);
        List<XGBStock> hs=xgbStockRepository.findByDayFormatOrderByContinueBoardCountDesc(yesterday);
        XGBStock hstock=null;
        if(hs!=null && hs.size()>0){
            hstock = hs.get(0);
        }
        return desc+end+"昨日情况 计提："+downStocks.size()+"连板:"+xs.size()+"<br>"+downStocks+"<br>最近5天市场情况<br>"+temperaturesClose+"<br>【信号123 注意集体高潮 相信数据】股吧数量:"+hotSortFive.size()+"<br>最高版:<br>"+hstock+"<br>"+hotSortFive+"end"+end+"<br>【信号123 注意集体高潮 相信数据】实时数量:"+myTgbStockFive.size()+"<br>"+myTgbStockFive+"<br>最近5天市场开盘情况<br>"+temperaturesOpen+":<br>"+temperatures+end+"<br>股吧热门:<br>"+tgbHots+"当日数量:"+downBeforeStocks.size()+"<br>"+downBeforeStocks;
    }
    @RequestMapping("/s/{format}")
    String s(@PathVariable("format")String format) {
        List<TgbStock> hotSort = tgbStockRepository.findByDayFormatOrderByHotSort(format);
        List<TgbStock> hotSeven = tgbStockRepository.findByDayFormatOrderByHotSevenDesc(format);
        List<TgbStock> openBidRate = tgbStockRepository.findByDayFormatOrderByOpenBidRateDesc(format);
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderById(format);
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
        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderById(format);
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
        marketService.temperatureClose();

        return "开盘:";
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
