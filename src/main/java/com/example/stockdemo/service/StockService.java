package com.example.stockdemo.service;

import com.example.stockdemo.mail.MailSendUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DecimalFormat;
@Component
public class StockService {
    @Autowired
    private RestTemplate restTemplate;
    private final static float TEN_PERCENT=1.1f;
    private Boolean sinajs(String code){
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
        //System.out.println(limit_up+":"+now);
        /*if(limit_up.equals(now)){
            System.out.println("good");
        }*/

        //System.out.println(str);
        return limit_up.equals(now);
    }
    public String taoguba() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("09H:");
        Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
        Elements elements = doc.getElementsByClass("tbleft");
        for(int i=0;i<20;i++){
            if(i==9){
                sb.append(" 24H:");
            }
            Element element = elements.get(i);
            String stockName = element.text();
            //sb.append(stockName).append(",");
            String url = element.getElementsByAttribute("href").attr("href");
            int length = url.length();
            String code = url.substring(length-9,length-1);
            if(sinajs(code)){
                System.out.println(stockName+":"+code);
                sb.append(stockName).append(",");
            }

        }
        MailSendUtil.sendMail(sb.toString());
        return sb.toString();
    }
}
