package com.example.stockdemo.domain;

public class StockInfo {
    private String code;
    private String marketCode;
    private String name;
    private String price;
    private String yesterdayPrice;
    private String openingPrice;
    private String closingPrice;
    private String sinaUrl;

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
