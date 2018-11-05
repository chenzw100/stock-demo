package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 股市看三日富可敌国，股市看两日稳定复利。
 * 昨天，今天，明天
 * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 */
@Entity(name="mystock")
public class MyStock {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String dayFormat;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int yesterdayClosePrice;

    @Column(nullable = true)
    private int todayOpenPrice;
    @Column(nullable = true)
    private int todayClosePrice;
    @Column(nullable = true)
    private int tomorrowOpenPrice;
    @Column(nullable = true)
    private int tomorrowClosePrice;
    @Column(nullable = true)
    private int openBidRate;
    @Column(nullable = true)
    private String continuous;

    @Transient
    private String todayOpenRate;
    @Transient
    private String todayCloseRate;
    @Transient
    private String tomorrowOpenRate;
    @Transient
    private String tomorrowCloseRate;
    @Transient
    private String sinaUrl;

    public String getContinuous() {
        return continuous;
    }

    public void setContinuous(String continuous) {
        this.continuous = continuous;
    }

    public MyStock(){
    }
    public MyStock(String code,String name){
        this.code =code;
        this.name = name;
        this.sinaUrl="https://hq.sinajs.cn/list="+code;
        this.created = MyUtils.getCurrentDate();
        this.dayFormat = DateFormatUtils.format(getCreated(), "yyyy-MM-dd");
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public int getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(int yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public int getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(int todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public int getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(int todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
    }

    public int getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(int tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
    }

    public int getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(int tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
    }
    public int getOpenBidRate() {
        openBidRate =MyUtils.getIncreaseRateCent(getTodayOpenPrice(),getYesterdayClosePrice()).intValue();
        return openBidRate;
    }

    public void setOpenBidRate(int openBidRate) {
        this.openBidRate = openBidRate;
    }
    public String getTodayOpenRate() {
        //(todayPrice-yesterdayPrice)/yesterdayPrice
        todayOpenRate =MyUtils.getYuanByCent(getOpenBidRate());
        return todayOpenRate;
    }

    public void setTodayOpenRate(String todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }

    public String getTodayCloseRate() {
        //(todayClosePrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTodayClosePrice(),getTodayOpenPrice()).toString();
    }

    public void setTodayCloseRate(String todayCloseRate) {
        this.todayCloseRate = todayCloseRate;
    }



    public String getTomorrowOpenRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTomorrowOpenPrice(),getTodayOpenPrice()).toString();
    }

    public void setTomorrowOpenRate(String tomorrowOpenRate) {
        this.tomorrowOpenRate = tomorrowOpenRate;
    }



    public String getTomorrowCloseRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTomorrowClosePrice(),getTodayOpenPrice()).toString();
    }

    public void setTomorrowCloseRate(String tomorrowCloseRate) {
        this.tomorrowCloseRate = tomorrowCloseRate;
    }

    public String getSinaUrl() {
        return sinaUrl;
    }

    public void setSinaUrl(String sinaUrl) {
        this.sinaUrl = sinaUrl;
    }
    public StringBuilder toChoice(StringBuilder sb){
        sb.append(code).append(name).append(",昨收:").append(MyUtils.getYuanByCent(getYesterdayClosePrice())).append("<br>");
        return sb;
    }
    public StringBuilder toOpen(StringBuilder sb){
        sb.append(code).append(name).append("连板:").append(continuous).append(",竞价:").append(getTodayOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toOpenTomorrow(StringBuilder sb){
        sb.append(code).append(name).append("连板:").append(continuous).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toClose(StringBuilder sb){
        sb.append(code).append(name).append("连板:").append(continuous).append(",开盘:").append(getTodayOpenRate())
                .append(",收盘:").append(getTodayCloseRate()).append("<br>");
        return sb;
    }
    public StringBuilder toCloseTomorrow(StringBuilder sb){
        sb.append(code).append(name).append("连板:").append(continuous).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).append("<br>");
        return sb;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(name).append("连板:").append(continuous).append(",竞价:").append(getTodayOpenRate())
        .append(",收盘:").append(getTodayCloseRate()).append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).append("<br>");
        return sb.toString();
    }


}
