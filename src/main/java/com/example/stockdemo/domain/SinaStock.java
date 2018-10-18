package com.example.stockdemo.domain;

import java.text.DecimalFormat;

/**
 * 收盘，开盘，当前
 */
public class SinaStock {
    private final static float TEN_PERCENT=1.1f;
    private String code;
    private String name;
    private String currentPrice;
    private String openingPrice;
    private String yesterdayClosingPrice;
    private Boolean isHarden;
    private String currentRate;
    public SinaStock(String code,String name,String openingPrice,String yesterdayClosingPrice,String currentPrice){
        this.code=code;
        this.name=name;
        this.currentPrice=currentPrice;
        this.openingPrice=openingPrice;
        this.yesterdayClosingPrice=yesterdayClosingPrice;
        Float yesterday =Float.parseFloat(yesterdayClosingPrice);
        Float harden = yesterday*TEN_PERCENT;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String limitUp=decimalFormat.format(harden)+"0";
        String now =currentPrice;
        this.isHarden = limitUp.equals(now);

        Float opening =Float.parseFloat(openingPrice);
        Float current =Float.parseFloat(currentPrice);
        Float rate = (current-opening)/opening*100;
        this.currentRate=decimalFormat.format(rate);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(String openingPrice) {
        this.openingPrice = openingPrice;
    }

    public String getYesterdayClosingPrice() {
        return yesterdayClosingPrice;
    }

    public void setYesterdayClosingPrice(String yesterdayClosingPrice) {
        this.yesterdayClosingPrice = yesterdayClosingPrice;
    }

    public Boolean getIsHarden() {
        return isHarden;
    }

    public void setIsHarden(Boolean isHarden) {
        this.isHarden = isHarden;
    }

    public String getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(String currentRate) {
        this.currentRate = currentRate;
    }
}
