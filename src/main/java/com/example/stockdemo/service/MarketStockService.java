package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.MyStockRepository;
import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.SinaStock;
import com.example.stockdemo.domain.StockInfo;
import com.example.stockdemo.mail.MailSendUtil;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MarketStockService {
    Log log = LogFactory.getLog(MarketStockService.class);
    private static Map<String, MyStock>  hotOpen = new HashMap();
    private static Map<String, MyStock>  tomorrow = new HashMap();
    private static Map<String, MyStock> today = new HashMap();
    private static Map<String, String> yesterdayLimitUp = new HashMap();
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MyStockRepository myStockRepository;
    public abstract Map getHopStock();
    public void closeLimitUp(){
        String urlCloseLimitUp = "https://wows-api.wallstreetcn.com/v2/sheet/board_stock?filter=true";
        Object response =  restTemplate.getForObject(urlCloseLimitUp, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            log.info(jsonArray.toArray()[0]+":"+jsonArray.toArray()[1]+":"+jsonArray.toArray()[12]);
            yesterdayLimitUp.put(jsonArray.toArray()[1].toString(),jsonArray.toArray()[12].toString());
        }
    }

    //下午3:15点后执行
    public String close(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");

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
            myStock.setTomorrowClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
            myStock.toCloseTomorrow(sb);
            myStockRepository.save(myStock);
        }

        tomorrow.clear();

        if(today.isEmpty()){
            List<MyStock> myStocks = myStockRepository.findByDayFormatOrderByOpenBidRateDesc(DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd"));
            if(myStocks!=null){
                for(MyStock myStock :myStocks){
                    today.put(myStock.getCode(),myStock);
                }
            }
        }
        sb.append("今天汇总：<br>");
        for (String code:today.keySet()){
            MyStock myStock = today.get(code);
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTodayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
            myStock.toClose(sb);
            myStock = myStockRepository.save(myStock);
            tomorrow.put(code,myStock);
        }
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
        MailSendUtil.sendMail(sb.toString());
        yesterdayLimitUp.clear();
        return sb.toString();
    }
    //8:45执行，获取的是昨天的数据
    public String choiceYesterday(){
        today.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("选股:<br>");
        hotOpen=getHopStock();
        for (String code:hotOpen.keySet()){
            MyStock myStock= hotOpen.get(code);
            SinaStock sinaStock = getSinaStock(code);
            if (sinaStock != null){
                if(sinaStock.getIsHarden()){
                    List<MyStock> myStocks= myStockRepository.findByCodeAndDayFormat(myStock.getCode(), myStock.getDayFormat());
                    if(myStocks!=null &&myStocks.size()>1){
                        myStock = myStocks.get(0);
                    }
                    myStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(sinaStock.getCurrentPrice()));
                    String continuous = yesterdayLimitUp.get(myStock.getName());
                    myStock.setContinuous(continuous);
                    myStock.toChoice(sb);
                    myStock= myStockRepository.save(myStock);
                    today.put(code,myStock);
                }
            }
        }
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
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
        String strName = stockObj[0].substring(stockObj[0].indexOf("=")+1,length);
        return new SinaStock(code,strName,stockObj[1],stockObj[2],stockObj[3]);
    }
    /**
     * 0:var hq_str_sz300668="杰恩设计,
     * 1：”27.55″，今日开盘价；
     2：”27.25″，昨日收盘价；
     3：”26.91″，当前价格；
     */

}
