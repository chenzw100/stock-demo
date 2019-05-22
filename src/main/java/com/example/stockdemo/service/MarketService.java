package com.example.stockdemo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.DownStockRepository;
import com.example.stockdemo.dao.StrongStocksDownRepository;
import com.example.stockdemo.dao.TemperatureRepository;
import com.example.stockdemo.dao.XgbStockRepository;
import com.example.stockdemo.domain.*;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class MarketService {
    Log log = LogFactory.getLog(MarketService.class);
    public static String  temperatureRecord = "";
    private static String temperature_url = "https://wows-api.wallstreetcn.com/v2/sheet/market_temperature?date=";
    private static String limit_url = "https://wows-api.wallstreetcn.com/statis_data/min_quote_change/limit?date=";
    private static String normal_url = "https://wows-api.wallstreetcn.com/statis_data/min_quote_change/normal?date=";
    private static String kline_url = "https://wows-api.wallstreetcn.com/sheet/min_kline?kline_type=a-stock-behavior-kline&date=";
    private static String multi_stock_url="https://wows-api.wallstreetcn.com/v2/sheet/multi_stock";
    private static String boom_stock_url ="https://wows-api.wallstreetcn.com/v2/sheet/boom_stock";
    private static String limit_down_url="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_down";
    private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";
    //https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution
    //https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TemperatureRepository temperatureRepository;
    @Autowired
    private StrongStocksDownRepository strongStocksDownRepository;
    @Autowired
    private DownStockRepository downStockRepository;
    @Autowired
    private XgbStockRepository xgbStockRepository;
    //3.10执行
    public String boomStock(){
        Object response =  restTemplate.getForObject(boom_stock_url, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        Set<XGBStock> ds=new TreeSet<XGBStock>();
        StrongStocksDown strongStocksDown = new StrongStocksDown();

        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String code = jsonArray.toArray()[0].toString().substring(0,6);
            xgbStock.setCode(code);
            String down = jsonArray.toArray()[4].toString();
            int downRate=MyUtils.getCentBySinaPriceStr(down);
            xgbStock.setDownRate(downRate);
            if(downRate<90){
                ds.add(xgbStock);
            }
            log.info("open:"+code +",downRate:"+downRate);

        }
        int i=0;
        String desc="";
        for(XGBStock xgbStock:ds){
            if(i==3){
                break;
            }
            desc=desc+xgbStock.getCode()+":"+xgbStock.getName()+":"+MyUtils.getYuanByCent(xgbStock.getDownRate())+"<br>";
            i++;
        }
        strongStocksDown.setDayFormat(MyUtils.getDayFormat());
        strongStocksDown.setStockDesc(desc);
        strongStocksDown.setDownCount(ds.size());
        strongStocksDown.setType(NumberEnum.StrongOpenType.OPEN.getCode());
        strongStocksDownRepository.save(strongStocksDown);
        return strongStocksDown.toString();
    }
    //3.10执行
    public String multiStock(){
        Object response =  restTemplate.getForObject(multi_stock_url, String.class);
        JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        Set<XGBStock> ds=new TreeSet<XGBStock>();
        StrongStocksDown strongStocksDown = new StrongStocksDown();

        for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            XGBStock xgbStock = new XGBStock();
            xgbStock.setName(jsonArray.toArray()[1].toString());
            String code = jsonArray.toArray()[0].toString().substring(0,6);
            xgbStock.setCode(code);
            String down = jsonArray.toArray()[4].toString();
            int downRate=MyUtils.getCentBySinaPriceStr(down);
            xgbStock.setDownRate(downRate);
            if(downRate<-900){
                ds.add(xgbStock);
            }
            log.info("strong:"+code + ",downRate:"+downRate);

        }
        int i=0;
        String desc="";
        for(XGBStock xgbStock:ds){
            if(i==3){
                break;
            }
            desc=desc+xgbStock.getCode()+":"+xgbStock.getName()+":"+MyUtils.getYuanByCent(xgbStock.getDownRate())+"<br>";
            i++;
        }
        strongStocksDown.setDayFormat(MyUtils.getDayFormat());
        strongStocksDown.setStockDesc(desc);
        strongStocksDown.setDownCount(ds.size());
        strongStocksDown.setType(NumberEnum.StrongOpenType.STRONG.getCode());
        strongStocksDownRepository.save(strongStocksDown);
        return strongStocksDown.toString();
    }
    //下午9:45-15:45点后执行
    public String temperature(int type)  {
        Temperature temperature = new Temperature(type);
        Date date = MyUtils.getCurrentDate();
        String dateStr = DateFormatUtils.format(date, "HH:mm:ss");
        String dateParam = MyUtils.getDayFormat();
        StringBuilder sb = new StringBuilder();

        String urlYesterday = kline_url+dateParam;
        Object response =  restTemplate.getForObject(urlYesterday, String.class);
        JSONArray arrayYesterday = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("datas");
        JSONObject jsonYesterdayLast = arrayYesterday.getJSONObject(arrayYesterday.size()-1);
        Double dYesterday = jsonYesterdayLast.getDouble("value")*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        sb.append(dateStr+"==>> [昨现:").append(decimalFormat.format(dYesterday));

        temperature.setYesterdayShow(MyUtils.getCentBySinaPriceStr(decimalFormat.format(dYesterday)));


        String urlTemperature = temperature_url+dateParam;
        response =  restTemplate.getForObject(urlTemperature,String.class);
        Integer temperatureNow = JSONObject.parseObject(response.toString()).getJSONObject("data").getInteger("temperature");
        sb.append("] [温度:").append(temperatureNow);

        temperature.setNowTemperature(temperatureNow);

        String urlLimit = limit_url+dateParam;
        response =  restTemplate.getForObject(urlLimit,String.class);
        JSONArray arrayLimit= JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("value");
        JSONArray vLimit = (JSONArray) arrayLimit.get(arrayLimit.size() - 1);
        Object down = vLimit.toArray()[2];
        Object raise = vLimit.toArray()[4];
        Object open = vLimit.toArray()[6];
        sb.append("] [涨停:").append(raise).append(", 跌停:").append(down).append(", 炸版:").append(open);

        temperature.setDownUp(Integer.valueOf(down.toString()));
        temperature.setRaiseUp(Integer.valueOf(raise.toString()));
        temperature.setOpen(Integer.valueOf(open.toString()));
        response =  restTemplate.getForObject(market_url, String.class);
        JSONObject detailInfo = JSONObject.parseObject(response.toString()).getJSONObject("data");
        int limitDownCount = detailInfo.getInteger("limit_down_count");
        int limitUpCount = detailInfo.getInteger("limit_up_count");
        temperature.setLimitDown(limitDownCount);
        temperature.setLimitUp(limitUpCount);
        response =  restTemplate.getForObject(broken_url, String.class);
        JSONArray arrayBroken = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("datas");
        JSONObject jsonBrokenLast = arrayBroken.getJSONObject(arrayBroken.size()-1);
        Double limitUpBrokenCount = jsonBrokenLast.getDouble("limit_up_broken_ratio")*100;
        temperature.setBrokenRatio(MyUtils.getCentBySinaPriceStr(decimalFormat.format(limitUpBrokenCount)));

        String urlNormal = normal_url+dateParam;
        response =  restTemplate.getForObject(urlNormal,String.class);
        JSONArray arrayNormal= JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("value");
        JSONArray vNormal = (JSONArray) arrayNormal.get(arrayNormal.size() - 1);
        Object downNormal = vNormal.toArray()[2];
        Object raiseNormal = vNormal.toArray()[1];
        sb.append("] [涨:").append(raiseNormal).append(", 跌:").append(downNormal).append("]<br>");
        String record=sb.toString();
        log.info("===>>record:"+record);
        temperature.setDown(Integer.valueOf(downNormal.toString()));
        temperature.setRaise(Integer.valueOf(raiseNormal.toString()));
        temperature.setTradeVal(currentTradeVal());
        temperature.setContinueVal(currentContinueVal());
        if(type==NumberEnum.TemperatureType.CLOSE.getCode()){
            List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getTomorrowDate()));
            temperature.setStrongDowns(downStocks.size());
            List<XGBStock> xgbStocks = xgbStockRepository.findByDayFormatAndContinueBoardCountGreaterThan(dateParam,1);
            if(xgbStocks!=null){
                temperature.setContinueCount(xgbStocks.size());
            }else {
                temperature.setContinueCount(0);
            }
        }else {
            temperature.setStrongDowns(0);
        }
        temperatureRepository.save(temperature);
        return record;
    }
    public String temperatureOpen()  {
        return temperature(NumberEnum.TemperatureType.OPEN.getCode());
    }
    public String temperatureClose()  {
        return temperature(NumberEnum.TemperatureType.CLOSE.getCode());
    }
    public String temperatureNormal()  {
        return temperature(NumberEnum.TemperatureType.NORMAL.getCode());
    }
    private Integer currentTradeVal() {
        String url ="http://qt.gtimg.cn/q=s_sh000001";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split("~");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return null;
        }
        str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    private String currentContinueVal() {
        String url ="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
        Object response =  restTemplate.getForObject(url,String.class);
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return null;
        }
        str =stockObj[5];
        return str;
    }
}
