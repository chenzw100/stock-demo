package com.example.stockdemo.service;

import com.example.stockdemo.domain.StockInfo;
import com.example.stockdemo.mail.MailSendUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class StockService {
    Log log = LogFactory.getLog(StockService.class);
    private static Map<String, StockInfo>  yesterday = new HashMap<String, StockInfo>();
    private static Map<String, StockInfo> today = new HashMap<String, StockInfo>();
    @Autowired
    private RestTemplate restTemplate;
    private final static float TEN_PERCENT=1.1f;
    public String chioceResut(){
        StringBuilder sb = new StringBuilder();
        sb.append("today：<br>");
        for (String code:today.keySet()){
            StockInfo stockInfo = today.get(code);
            //https://www.taoguba.com.cn/quotes/sz002937/
            sb.append(code).append(stockInfo.getName()).
            append(" 昨收:").append(stockInfo.getYesterdayPrice()).append(",开盘:") .append(stockInfo.getOpeningPrice()).append("开幅:").append(stockInfo.getOpenRate())
                    .append(" <a href='https://www.taoguba.com.cn/quotes/" + stockInfo.getCode() + "' target='_blank'>" + stockInfo.getName() + "</a><br>");
        }
        sb.append("yesterday:<br>");
        for (String code:yesterday.keySet()){
            StockInfo stockInfo = yesterday.get(code);
            Object response =  restTemplate.getForObject(stockInfo.getSinaUrl(),String.class);
            String str = response.toString();
            String[] stockObj = str.split(",");
            sb.append(code).append(stockInfo.getName()).
            append(" 昨收:").append(stockInfo.getYesterdayPrice()).append(",昨开:")
                    .append(stockInfo.getOpeningPrice()).append("今开:").append(stockObj[1])
                    .append(" <a href='https://www.taoguba.com.cn/quotes/" + stockInfo.getCode() + "' target='_blank'>" + stockInfo.getName() + "</a><br>");
        }
        return sb.toString();
    }
    //下午3:15点后执行
    public String close(){
        StringBuilder sb = new StringBuilder();
        sb.append("收盘汇总：<br>");
        for (String code:today.keySet()){
            StockInfo stockInfo = today.get(code);
            Object response =  restTemplate.getForObject(stockInfo.getSinaUrl(),String.class);
            String str = response.toString();
            //System.out.println(str);
            String[] stockObj = str.split(",");
            stockInfo.setClosingPrice(stockObj[3]);
            stockInfo.setOpeningPrice(stockObj[1]);
            stockInfo.setPrice(stockObj[3]);
            yesterday.put(code,stockInfo);
            sb.append(code).append(stockInfo.getName()).
            append(" 昨收:").append(stockInfo.getYesterdayPrice()).append(",开盘:")
                    .append(stockInfo.getOpeningPrice()).append(",开幅:").append(stockInfo.getOpenRate()).append(",收盘:").append(stockInfo.getClosingPrice()).append(",收幅:").append(stockInfo.getCloseRate()).append("<br>");
        }
        MailSendUtil.sendMail(sb.append(MarketService.temperatureRecord).toString());
        return sb.toString();
    }
    //9:26执行
    public String open(){
        StringBuilder sb = new StringBuilder();
        sb.append("开盘竞价：<br>");
        for (String code:today.keySet()){
            StockInfo stockInfo = today.get(code);
            //选出来后，新的价格新的一天
            Object response =  restTemplate.getForObject(stockInfo.getSinaUrl(),String.class);
            String str = response.toString();
            String[] stockObj = str.split(",");
            stockInfo.setOpeningPrice(stockObj[1]);
            stockInfo.setYesterdayPrice(stockObj[2]);
            stockInfo.setClosingPrice(stockObj[3]);
            stockInfo.setPrice(stockObj[3]);
            sb.append(code).append(stockInfo.getName()).
            append(" 昨收:").append(stockInfo.getYesterdayPrice()).append(",开盘:")
                    .append(stockInfo.getOpeningPrice()).append(",开幅:").append(stockInfo.getOpenRate()).append("<br>");
        }
        sb.append("上一天情况:<br>");
        for (String code:yesterday.keySet()){
            StockInfo stockInfo = yesterday.get(code);
            String yesterdayOpenRate = stockInfo.getOpenRate();
            Object response =  restTemplate.getForObject(stockInfo.getSinaUrl(),String.class);
            String str = response.toString();
            String[] stockObj = str.split(",");
            stockInfo.setYesterdayPrice(stockObj[2]);
            Float yesterdayOpening =Float.parseFloat(stockInfo.getOpeningPrice());
            Float opening =Float.parseFloat(stockObj[1]);
            Float rate = (opening-yesterdayOpening)/yesterdayOpening*100;
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            sb.append(code).append(stockInfo.getName()).
            append(" 昨收:").append(stockInfo.getYesterdayPrice()).append(",昨开:")
                    .append(stockInfo.getOpeningPrice()).append(",昨日开幅:").append(yesterdayOpenRate).append(",今开:").append(stockObj[1]).append(",赢幅:").append(decimalFormat.format(rate)).append("<br>");
        }
        yesterday.clear();
        MailSendUtil.sendMail(sb.toString());
        return sb.toString();
    }
    //8:45执行，获取的是昨天的数据
    public String choice() throws IOException {
        today.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("09H:<br>");
        Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
        Elements elements = doc.getElementsByClass("tbleft");
        for(int i=0;i<20;i++){
            if(i==9){
                sb.append("<br>24H:");
            }
            Element element = elements.get(i);
            String stockName = element.text();
            //sb.append(stockName).append(",");
            String url = element.getElementsByAttribute("href").attr("href");
            int length = url.length();
            String code = url.substring(length-9,length-1);
            if(sinajs(code,stockName)){
                StockInfo stockInfo = today.get(code);
                log.info("choice:【"+stockName+":"+code+"】昨收:"+stockInfo.getYesterdayPrice());
                sb.append(stockName).append(",昨收:").append(stockInfo.getYesterdayPrice()).append("<br>");
            }

        }
        MailSendUtil.sendMail(sb.toString());
        return sb.toString();
    }
    private Boolean sinajs(String code,String stockName){
        //log.info(stockName+":"+code+",request.");
        if(today.containsKey(code)){
            log.info(stockName+"is contains "+code);
            return true;
        }
        String url ="https://hq.sinajs.cn/list="+code;
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        //System.out.println(str);
        String[] stockObj = str.split(",");
        if(stockObj.length<3){
            return false;
        }
        Float yesterday =Float.parseFloat(stockObj[2]);
        Float harden = yesterday*TEN_PERCENT;
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String limit_up=decimalFormat.format(harden)+"0";
        String now =stockObj[3];

        log.info(code+stockName+"::limit_up:"+limit_up+":now:"+now);
        Boolean isHarden = limit_up.equals(now);
        if(isHarden){
            StockInfo info = new StockInfo();
            info.setSinaUrl(url);
            info.setMarketCode(code);
            info.setCode(code);
            info.setYesterdayPrice(stockObj[2]);
            info.setClosingPrice(stockObj[3]);
            info.setOpeningPrice(stockObj[1]);
            info.setPrice(stockObj[3]);
            info.setName(stockName);
            today.put(code,info);
        }
        return isHarden;
    }
    /**
     * 1：”27.55″，今日开盘价；
     2：”27.25″，昨日收盘价；
     3：”26.91″，当前价格；
     */

}
