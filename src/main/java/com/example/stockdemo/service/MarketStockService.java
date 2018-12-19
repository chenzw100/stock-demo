package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.DownStockRepository;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public abstract class MarketStockService {
    Log log = LogFactory.getLog(MarketStockService.class);
    private static Map<String, MyStock>  hotOpen = new HashMap();
    private static Map<String, MyStock>  tomorrow = new HashMap();
    private static Map<String, MyStock> today = new HashMap();
    private static Map<String, XGBStock> yesterdayLimitUp = new HashMap();
    private static Set<DownStock> down=new TreeSet<DownStock>();
    private static String multi_stock_url="https://wows-api.wallstreetcn.com/v2/sheet/multi_stock";
    private static String boom_stock_url ="https://wows-api.wallstreetcn.com/v2/sheet/boom_stock";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MyStockRepository myStockRepository;
    @Autowired
    DownStockRepository downStockRepository;
    public abstract Map getHopStock();
    public String downChoice(){
        multiStock();
        boomStock();
        return "";
    }

    //3.10执行
    public String boomStock(){
        Object response =  restTemplate.getForObject(boom_stock_url, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        Set<XGBStock> ds=new TreeSet<XGBStock>();

        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String codeStr = jsonArray.toArray()[0].toString();
            String code = codeStr.substring(0, 6);
            if(codeStr.contains("Z")){
                xgbStock.setCode("sz"+code);
            }else {
                xgbStock.setCode("sh"+code);
            }
            xgbStock.setPrice(jsonArray.toArray()[3].toString());
            String down = jsonArray.toArray()[4].toString();
            int downRate=MyUtils.getCentBySinaPriceStr(down);
            xgbStock.setDownRate(downRate);
            if(downRate<90){
                ds.add(xgbStock);
            }
            log.info("open:"+code +",downRate:"+downRate);

        }

        for(XGBStock xgbStock:ds){
            DownStock downStock = xgbStock.coverDownStock();
            downStock.setStockType(NumberEnum.StockType.OPEN.getCode());
            if(xgbStock.getDownRate()<-910){
               List<DownStock> downStocks = downStockRepository.findByCodeAndDayFormat(downStock.getCode(), downStock.getDayFormat());
                if(downStocks!=null&& downStocks.size()>0){
                    downStock = downStocks.get(0);
                    downStock.setStockType(NumberEnum.StockType.DOWN.getCode());
                }
            }
            downStockRepository.save(downStock);
        }

        return "";
    }
    //3.10执行
    public String multiStock(){
        Object response =  restTemplate.getForObject(multi_stock_url, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        Set<XGBStock> ds=new TreeSet<XGBStock>();

        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String codeStr = jsonArray.toArray()[0].toString();
            String code = codeStr.substring(0, 6);
            if(codeStr.contains("Z")){
                xgbStock.setCode("sz"+code);
            }else {
                xgbStock.setCode("sh"+code);
            }
            xgbStock.setPrice(jsonArray.toArray()[3].toString());
            String down = jsonArray.toArray()[4].toString();
            int downRate=MyUtils.getCentBySinaPriceStr(down);
            xgbStock.setDownRate(downRate);
            if(downRate<-910){
                ds.add(xgbStock);
            }
            log.info("strong:"+code + ",downRate:"+downRate);

        }

        for(XGBStock xgbStock:ds){
            DownStock downStock = xgbStock.coverDownStock();
            downStock.setStockType(NumberEnum.StockType.STRONG.getCode());
            downStockRepository.save(downStock);
        }

        return "";
    }
    public void closeLimitUp(){
        String urlCloseLimitUp = "https://wows-api.wallstreetcn.com/v2/sheet/board_stock?filter=true";
        Object response =  restTemplate.getForObject(urlCloseLimitUp, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String code = jsonArray.toArray()[0].toString().substring(0,6);
            xgbStock.setCode(code);
            log.info(code+ ":" + jsonArray.toArray()[11] + ":" + jsonArray.toArray()[12]);
            xgbStock.setOpenCount(Integer.parseInt(jsonArray.toArray()[11].toString()));
            xgbStock.setContinueBoardCount(Integer.parseInt(jsonArray.toArray()[12].toString()));
            yesterdayLimitUp.put(code,xgbStock);
        }
    }

    //下午3:15点后执行
    public String close(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");

        List<MyStock> myStocksTomorrow = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyy-MM-dd"));
        if(myStocksTomorrow!=null){
            for(MyStock myStock :myStocksTomorrow){
                SinaStock sinaStock = getSinaStock(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                myStock.toCloseTomorrow(sb);
                myStockRepository.save(myStock);
            }
        }
        tomorrow.clear();
        sb.append("今天汇总：<br>");
        List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd"));
        if(myStocks!=null){
            for(MyStock myStock :myStocks){
                today.put(myStock.getCode(),myStock);
                SinaStock sinaStock = getSinaStock(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                myStock.toClose(sb);
                myStockRepository.save(myStock);
            }
        }
        tomorrow=today;
        sb.append(MarketService.temperatureRecord);
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
        closeLimitUp();
        return sb.toString();
    }
    //9:26执行
    public String open(){
        StringBuilder sb = new StringBuilder();
        sb.append("开盘竞价：<br>");
        for (String code:today.keySet()){
            MyStock myStock = today.get(code);
            //选出来后，新的价格新的一天
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(sinaStock.getOpeningPrice()));
            myStock.toOpen(sb);
            myStock= myStockRepository.save(myStock);
            today.put(code,myStock);
        }
        sb.append("明天情况:<br>");
        if(tomorrow.isEmpty()){
            List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyy-MM-dd"));
            if(myStocks!=null){
                for(MyStock myStock :myStocks){
                    tomorrow.put(myStock.getCode(),myStock);
                }
            }
        }
        for (String code:tomorrow.keySet()){
            MyStock myStock = tomorrow.get(code);
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(sinaStock.getOpeningPrice()));
            myStock.toOpenTomorrow(sb);
            myStock = myStockRepository.save(myStock);
            tomorrow.put(code,myStock);
        }
        log.info(sb.toString());
       // MailSendUtil.sendMail(sb.toString());
        yesterdayLimitUp.clear();
        return sb.toString();
    }
    //8:45执行，获取的是昨天的数据
    public String choiceYesterday(){
        today.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("选股:<br>");
        hotOpen=getHopStock();
        int i=0;
        for (String code:hotOpen.keySet()){
            MyStock myStock= hotOpen.get(code);
            i++;
            log.info(i+":"+code+":"+myStock.getName());
            myStock.setCreated(new Date());
            SinaStock sinaStock = getSinaStock(code);
            if (sinaStock != null){
                if(sinaStock.getIsHarden()){
                    List<MyStock> myStocks= myStockRepository.findByCodeAndDayFormat(myStock.getCode(), myStock.getDayFormat());
                    if(myStocks!=null &&myStocks.size()>1){
                        myStock = myStocks.get(0);
                    }
                    myStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                    XGBStock xgbStock = yesterdayLimitUp.get(myStock.getCode().substring(2,8));
                    myStock.setContinuous(xgbStock.getContinueBoardCount().toString());
                    myStock.setOpenCount(xgbStock.getOpenCount());
                    myStock= myStockRepository.save(myStock);
                    myStock.toChoice(sb);
                    today.put(code, myStock);
                }
            }
        }
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
        return sb.toString();
    }

    public String openDown(){
        today.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("选股:<br>");
        List<DownStock> downStocks = downStockRepository.findByDayFormatOrderByOpenBidRateDesc(MyUtils.getDayFormat());

        sb.append("开盘竞价：<br>");
        for (DownStock downStock:downStocks){
            //选出来后，新的价格新的一天
            SinaStock sinaStock = getSinaStock(downStock.getCode());
            downStock.setTodayOpenPrice(MyUtils.getCentBySinaPriceStr(sinaStock.getOpeningPrice()));
            downStock.toOpen(sb);
            downStockRepository.save(downStock);
        }
        sb.append("明天情况:<br>");
        if(tomorrow.isEmpty()){
            List<DownStock> downStocksTomorrow = downStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyy-MM-dd"));
            if(downStocksTomorrow!=null){
                for(DownStock downStock :downStocksTomorrow){
                    SinaStock sinaStock = getSinaStock(downStock.getCode());
                    downStock.setTomorrowOpenPrice(MyUtils.getCentBySinaPriceStr(sinaStock.getOpeningPrice()));
                    downStock.toOpenTomorrow(sb);
                    downStockRepository.save(downStock);
                }
            }
        }

        log.info(sb.toString());
        return sb.toString();
    }
    public String closeDown(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");
        List<DownStock> myStocksTomorrow = downStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getYesterdayDate(), "yyyy-MM-dd"));
        if(myStocksTomorrow!=null){
            for(DownStock myStock :myStocksTomorrow){
                SinaStock sinaStock = getSinaStock(myStock.getCode());
                myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                myStock.toCloseTomorrow(sb);
                downStockRepository.save(myStock);
            }
        }
        sb.append("今天汇总：<br>");
        List<DownStock> myStocks = downStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd"));
        if(myStocks!=null){
            for(DownStock myStock :myStocks){
                SinaStock sinaStock = getSinaStock(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                myStock.toClose(sb);
                downStockRepository.save(myStock);
            }
        }
        log.info(sb.toString());
        return sb.toString();
    }
    private SinaStock getSinaStock(String code) {
        String url ="https://hq.sinajs.cn/list="+code;
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<3){
            log.error(code + ":err=" + str);
            return null;
        }
        int length = stockObj[0].length();
        String strName = stockObj[0].substring(stockObj[0].indexOf("=")+2,length);
        return new SinaStock(code,strName,stockObj[1],stockObj[2],stockObj[3]);
    }
    /**
     * 0:var hq_str_sz300668="杰恩设计,
     * 1：”27.55″，今日开盘价；
     2：”27.25″，昨日收盘价；
     3：”26.91″，当前价格；
     */

    public  void currentTime(){

    }
    public  void dayTime(){

    }
    public  void clearTime(){

    }
}
