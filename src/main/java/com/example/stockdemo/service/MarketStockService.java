package com.example.stockdemo.service;

import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.SinaStock;
import com.example.stockdemo.mail.MailSendUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public abstract class MarketStockService {
    Log log = LogFactory.getLog(MarketStockService.class);
    private static Map<String, MyStock>  hotOpen = new HashMap();
    private static Map<String, MyStock>  tomorrow = new HashMap();
    private static Map<String, MyStock> today = new HashMap();
    @Autowired
    RestTemplate restTemplate;
    public abstract Map getHopStock();

    //下午3:15点后执行
    public String close(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        sb.append("明天汇总：<br>");
        for (String code:tomorrow.keySet()){
            MyStock myStock = tomorrow.get(code);
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTomorrowClosePrice(sinaStock.getCurrentPrice());
            myStock.toCloseTomorrow(sb);
        }

        tomorrow.clear();
        sb.append("今天汇总：<br>");
        for (String code:today.keySet()){
            MyStock myStock = today.get(code);
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTodayClosePrice(sinaStock.getCurrentPrice());
            tomorrow.put(code,myStock);
            myStock.toClose(sb);
        }
        sb.append(MarketService.temperatureRecord);
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
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
            myStock.setTodayOpenPrice(sinaStock.getOpeningPrice());
            myStock.toOpen(sb);

        }
        sb.append("明天情况:<br>");
        for (String code:tomorrow.keySet()){
            MyStock myStock = tomorrow.get(code);
            SinaStock sinaStock = getSinaStock(code);
            myStock.setTomorrowOpenPrice(sinaStock.getOpeningPrice());
            myStock.toOpenTomorrow(sb);
        }
        log.info(sb.toString());
        MailSendUtil.sendMail(sb.toString());
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
                    myStock.setYesterdayPrice(sinaStock.getCurrentPrice());
                    today.put(code,myStock);
                    myStock.toChoice(sb);
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
