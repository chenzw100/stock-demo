package com.example.stockdemo;

import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.enums.NumberEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by laikui on 2018/10/16.
 */
public class TestMain {
    static Log log = LogFactory.getLog(TestMain.class);
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                //System.out.println(tds.get(2).text()+":"+tds.get(3).text());
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                TgbStock tgbStock = new TgbStock(code,stockName);
                tgbStock.setStockType(NumberEnum.StockType.DAY.getCode());
                tgbStock.setHotSort(i - 9);
                tgbStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                tgbStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                log.info(i+":"+code+":"+stockName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("==>重新执行");
        }
        /*String response = "{\"code\":200,\"data\":{\"fields\":[\"symbol\",\"title\",\"reason\",\"cur_price\",\"px_change_rate\",\"blockade_ratio\",\"turnover_ratio\",\"circulation_value\",\"total_value\",\"first_raise_time\",\"last_raise_time\",\"open_count\",\"continue_board_count\",\"bool_new_stock\",\"block_intensity\",\"plates_reason\"],\"items\":[[\"002263.SZ\",\"*ST东南\",\"此前连续12日跌停，集团质押遭平仓\",1.65,5.095541401273875,0.000026086584782119254,3.153146300328675,3099294165,3099294165,1541385159,1541398245,7,1,false,0.010434633912847702,[{\"plate_id\":24898553,\"plate_name\":\"ST股\"}]],[\"000622.SZ\",\"恒立实业\",\"国内最早从事汽车空调零部件行业的企业；民企；第一大股东为中国华阳投资控股有限公司\",5.53,9.940357852882704,0.09250769473174265,0.6349148452822734,2351499780,2351499780,1541381400,1541381400,0,8,false,37.003077892697064,[{\"plate_id\":2843278,\"plate_name\":\"壳公司\"},{\"plate_id\":22082034,\"plate_name\":\"汽车零部件\"}]]]}}";

        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        for(int i=0;i<closeLimitUp.size();i++){
                JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
                System.out.println(jsonArray.toArray()[0].toString().substring(0,6)+":"+jsonArray.toArray()[15]);
                JSONArray jsonArrayPlate = jsonArray.getJSONArray(15);
                String plateName ="";
                for(int j=0;j<jsonArrayPlate.size();j++){
                    plateName = plateName+","+jsonArrayPlate.getJSONObject(j).getString("plate_name");
                }
                plateName =plateName.substring(1,plateName.length());
                System.out.println(plateName);
        }*/
/*
        Float yesterday =Float.parseFloat("9.920");
        Float opening =Float.parseFloat("8.490");
        Float rate = (opening-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        System.out.println(rate+":"+ decimalFormat.format(rate));*/
       /* String response = "{\"code\":200,\"data\":{\"fields\":[\"symbol\",\"title\",\"reason\",\"cur_price\",\"px_change_rate\",\"blockade_ratio\",\"turnover_ratio\",\"circulation_value\",\"total_value\",\"first_raise_time\",\"last_raise_time\",\"open_count\",\"continue_board_count\",\"bool_new_stock\",\"block_intensity\",\"plates_reason\"],\"items\":[[\"002263.SZ\",\"*ST东南\",\"此前连续12日跌停，集团质押遭平仓\",1.65,5.095541401273875,0.000026086584782119254,3.153146300328675,3099294165,3099294165,1541385159,1541398245,7,1,false,0.010434633912847702,[{\"plate_id\":24898553,\"plate_name\":\"ST股\"}]],[\"000622.SZ\",\"恒立实业\",\"国内最早从事汽车空调零部件行业的企业；民企；第一大股东为中国华阳投资控股有限公司\",5.53,9.940357852882704,0.09250769473174265,0.6349148452822734,2351499780,2351499780,1541381400,1541381400,0,8,false,37.003077892697064,[{\"plate_id\":2843278,\"plate_name\":\"壳公司\"},{\"plate_id\":22082034,\"plate_name\":\"汽车零部件\"}]]]}}";
    JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
    for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            System.out.println(jsonArray.toArray()[0].toString().substring(0,6)+":"+jsonArray.toArray()[12]);
    }*/
       // System.out.println(MyUtils.getCentBySinaPriceStr("-9.947643979057595")<-995);
        /*Set<XGBStock> ds=new TreeSet<XGBStock>();
        XGBStock xgbStock = new XGBStock();
        xgbStock.setName("1111");
        String code = "1111";
        xgbStock.setCode(code);
        String down = "0.89";
        int downRate=MyUtils.getCentBySinaPriceStr(down);
        xgbStock.setDownRate(downRate);
        if(downRate<90){
            ds.add(xgbStock);
        }

        XGBStock xgbStock1 = new XGBStock();
        xgbStock1.setName("1112");
        xgbStock1.setCode("1112");
        downRate=MyUtils.getCentBySinaPriceStr("10.01");
        xgbStock1.setDownRate(downRate);
        if(downRate<90){
            ds.add(xgbStock1);
        }

        XGBStock xgbStock2 = new XGBStock();
        xgbStock2.setName("1113");
        xgbStock2.setCode("1113");
        downRate=MyUtils.getCentBySinaPriceStr("-1.95");
        xgbStock2.setDownRate(downRate);
        if(downRate<90){
            ds.add(xgbStock2);
        }

        XGBStock xgbStock3 = new XGBStock();
        xgbStock3.setName("1114");
        xgbStock3.setCode("1114");
        downRate=MyUtils.getCentBySinaPriceStr("-6.95");
        xgbStock3.setDownRate(downRate);
        if(downRate<90){
            ds.add(xgbStock3);
        }
        XGBStock xgbStock4 = new XGBStock();
        xgbStock4.setName("1115");
        xgbStock4.setCode("1115");
        downRate=MyUtils.getCentBySinaPriceStr("0.2");
        xgbStock4.setDownRate(downRate);
        if(downRate<90){
            ds.add(xgbStock4);
        }
        for(XGBStock stock:ds){
            System.out.println(stock.getCode()+":"+stock.getName()+":"+MyUtils.getYuanByCent(stock.getDownRate()));
        }*/
        /*String codeStr = "5566.SS";
        if(codeStr.contains("Z")){
            System.out.printf("111");
        }else {
            System.out.printf("222");
        }*/
    }
}
