package com.example.stockdemo.service.xgb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.stockdemo.dao.*;
import com.example.stockdemo.domain.DownStock;
import com.example.stockdemo.domain.SpaceHeight;
import com.example.stockdemo.domain.Temperature;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.service.dfcf.DfcfService;
import com.example.stockdemo.service.qt.QtService;
import com.example.stockdemo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */

@Component
public class XgbService extends QtService {
    private static String limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up";
    private static String limit_up_broken ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up_broken";

   // private static String limit_down="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_down";
    private static String super_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=super_stock";

    //private static String yesterday_limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=yesterday_limit_up";yesterday_limit_up_avg_pcp
    private static String market_temperature="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=market_temperature,limit_up_broken_count,limit_up_broken_ratio,rise_count,fall_count,limit_down_count,limit_up_count";

    //private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    //private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";

    @Autowired
    TemperatureRepository temperatureRepository;
    @Autowired
    DfcfService dfcfService;
    @Autowired
    XgbStockRepository xgbStockRepository;
    @Autowired
    SpaceHeightRepository spaceHeightRepository;
    @Autowired
    DownStockRepository downStockRepository;
    @Autowired
    XgbFiveUpStockRepository xgbFiveUpStockRepository;

    public void temperature(int type)  {
        Temperature temperature = new Temperature(type);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        Object response = getRequest(market_temperature);
        if(response!=null){
            JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
            JSONObject jsonDataLast = array.getJSONObject(array.size() - 1);

            int limitDownCount = jsonDataLast.getInteger("limit_down_count");
            int limitUpCount = jsonDataLast.getInteger("limit_up_count");
            temperature.setLimitDown(limitDownCount);
            temperature.setDownUp(limitDownCount);
            temperature.setLimitUp(limitUpCount);
            temperature.setRaiseUp(limitUpCount);
            temperature.setDown(jsonDataLast.getInteger("fall_count"));
            temperature.setRaise(jsonDataLast.getInteger("rise_count"));

            Double limitUpBrokenCount = jsonDataLast.getDouble("limit_up_broken_ratio")*100;
            temperature.setBrokenRatio(MyUtils.getCentBySinaPriceStr(decimalFormat.format(limitUpBrokenCount)));
            temperature.setOpen(jsonDataLast.getInteger("limit_up_broken_count"));

            array = JSONObject.parseObject(response.toString()).getJSONArray("data");
            jsonDataLast = array.getJSONObject(array.size() - 1);
            String temperatureNum = jsonDataLast.getString("market_temperature");
            temperature.setNowTemperature(MyUtils.getCentBySinaPriceStr(temperatureNum));
        }

        temperature.setContinueVal(dfcfService.currentContinueVal());
        temperature.setYesterdayShow(MyUtils.getCentByYuanStr(dfcfService.currentYesterdayVal()));
        temperature.setTradeVal(currentTradeVal());

        if(type==NumberEnum.TemperatureType.CLOSE.getCode()){
            List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(MyUtils.getDayFormat(MyUtils.getTomorrowDate()));
            temperature.setStrongDowns(downStocks.size());
            List<XGBStock> xgbStocks = xgbStockRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getDayFormat(),1);
            if(xgbStocks!=null){
                temperature.setContinueCount(xgbStocks.size());
            }else {
                temperature.setContinueCount(0);
            }
        }else {
            temperature.setStrongDowns(0);
        }

        temperatureRepository.save(temperature);
    }

    public void limitUp(){
        XGBStock spaceHeightStock=null;
        int spaceHeight = 1;
        Object response = getRequest(limit_up);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");

        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            if(!name.contains("S")) {
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                XGBStock xgbStock = new XGBStock(code,name);
                xgbStock.setCreated(new Date());
                if (codeStr.contains("Z")) {
                    xgbStock.setCode("sz" + code);
                } else {
                    xgbStock.setCode("sh" + code);
                }
                log.info(i+":zt==================>"+xgbStock.getCode());
                xgbStock.setOpenCount(jsonStock.getInteger("break_limit_up_times"));
                xgbStock.setContinueBoardCount(jsonStock.getInteger("limit_up_days"));
                xgbStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));

                JSONObject jsonReason = jsonStock.getJSONObject("surge_reason");
                JSONArray jsonPlateArray = jsonReason.getJSONArray("related_plates");
                int length = jsonPlateArray.size();
                String plateName="";
                for (int j = 0; j < length; j++) {
                    JSONObject jsonPalte = (JSONObject) jsonPlateArray.get(j);
                    plateName+=jsonPalte.getString("plate_name");
                }
               // xgbStock.setPlateName(jsonReason.getString("stock_reason"));
                xgbStock.setPlateName(plateName);
                xgbStockRepository.save(xgbStock);
                if (xgbStock.getContinueBoardCount() > spaceHeight) {
                    spaceHeightStock = xgbStock;
                    spaceHeight = xgbStock.getContinueBoardCount();
                }
                if(xgbStock.getContinueBoardCount()>4){
                    xgbFiveUpStockRepository.save(xgbStock.toXgbFiveUpStock());
                }
            }
        }
        if(spaceHeightStock!=null){
            spaceHeight(spaceHeightStock);
        }
    }
    void spaceHeight(XGBStock hstock) {
        SpaceHeight spaceHeight = new SpaceHeight(hstock.getCode(),hstock.getName());
        spaceHeight.setFirstOpen(hstock.getOpenCount());
        spaceHeight.setFirstContinue(hstock.getContinueBoardCount());
        spaceHeight.setFirstPlate(hstock.getPlateName());
        spaceHeight.setCreated(hstock.getCreated());
        spaceHeight.setDayFormat(hstock.getDayFormat());
        spaceHeightRepository.save(spaceHeight);
    }
    public void limitUpBroken(){

        Object response = getRequest(limit_up_broken);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");

        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            log.info(name+"-broken---->"+cp);
            if(!name.contains("S") && cp<0) {
                DownStock downStock = new DownStock();
                downStock.setStockType(NumberEnum.StockType.OPEN.getCode());
                downStock.setCreated(MyUtils.getCurrentDate());
                downStock.setDayFormat(MyUtils.getDayFormat(MyUtils.getTomorrowDate()));
                downStock.setPreFormat(MyUtils.getDayFormat());
                downStock.setName(name);
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                if (codeStr.contains("Z")) {
                    downStock.setCode("sz" + code);
                } else {
                    downStock.setCode("sh" + code);
                }
                downStock.setCode(code);
                downStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));
                downStock.setDownRate(cp*100);
                downStockRepository.save(downStock);

            }

        }
    }
    public void superStock(){

        Object response = getRequest(super_stock);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");

        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            log.info(name+"-->super"+cp);
            if(!name.contains("S") && cp<-8) {
                DownStock downStock = new DownStock();
                downStock.setStockType(NumberEnum.StockType.STRONG.getCode());
                downStock.setCreated(MyUtils.getCurrentDate());
                downStock.setDayFormat(MyUtils.getDayFormat(MyUtils.getTomorrowDate()));
                downStock.setPreFormat(MyUtils.getDayFormat());
                downStock.setName(name);
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                if (codeStr.contains("Z")) {
                    downStock.setCode("sz" + code);
                } else {
                    downStock.setCode("sh" + code);
                }
                downStock.setCode(code);
                downStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));
                downStock.setDownRate(cp*100);
                downStockRepository.save(downStock);

            }

        }
    }

}
