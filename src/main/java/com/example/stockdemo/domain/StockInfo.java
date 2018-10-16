package com.example.stockdemo.domain;

import java.text.DecimalFormat;

public class StockInfo {
    private String code;
    private String marketCode;
    private String name;
    private String price;
    private String yesterdayPrice;
    private String openingPrice;
    private String closingPrice;
    private String sinaUrl;
    private String openRate;
    private String closeRate;

    public String getCloseRate() {
        Float yesterday =Float.parseFloat(getYesterdayPrice());
        Float closing =Float.parseFloat(getClosingPrice());
        Float rate = (closing-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        closeRate=decimalFormat.format(rate);
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public String getOpenRate() {
        Float yesterday =Float.parseFloat(getYesterdayPrice());
        Float opening =Float.parseFloat(getOpeningPrice());
        Float rate = (opening-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        openRate=decimalFormat.format(rate);
        return openRate;
    }

    public void setOpenRate(String openRate) {
        this.openRate = openRate;
    }

    public String getSinaUrl() {
        return sinaUrl;
    }

    public void setSinaUrl(String sinaUrl) {
        this.sinaUrl = sinaUrl;
    }

    public String getCode() {
        return code;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(String yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(String openingPrice) {
        this.openingPrice = openingPrice;
    }

    public String getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(String closingPrice) {
        this.closingPrice = closingPrice;
    }
}
