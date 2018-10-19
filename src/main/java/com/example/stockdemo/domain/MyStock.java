package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.DecimalFormat;

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
    @Column(nullable = true)
    private String dayFormat;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String todayOpenPrice;
    @Column(nullable = true)
    private String todayClosePrice;
    @Column(nullable = true)
    private String tomorrowOpenPrice;
    @Column(nullable = true)
    private String tomorrowClosePrice;
    @Column(nullable = true)
    private String yesterdayPrice;
    @Column(nullable = true)
    private String todayOpenRate;
    @Column(nullable = true)
    private String todayCloseRate;
    @Column(nullable = true)
    private String tomorrowOpenRate;
    @Column(nullable = true)
    private String tomorrowCloseRate;
    private String sinaUrl;
    public MyStock(){
    }
    public MyStock(String code,String name){
        this.code =code;
        this.name = name;
        this.sinaUrl="https://hq.sinajs.cn/list="+code;
        this.dayFormat = DateFormatUtils.format(MyUtils.getCurrentDate(), "yyyy-MM-dd");
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

    public String getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(String todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public String getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(String tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
    }

    public String getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(String yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getTodayOpenRate() {
        //(todayPrice-yesterdayPrice)/yesterdayPrice
        Float yesterday =Float.parseFloat(getYesterdayPrice());
        Float todayPrice =Float.parseFloat(getTodayOpenPrice());
        Float rate = (todayPrice-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        todayOpenRate=decimalFormat.format(rate);
        return todayOpenRate;
    }

    public void setTodayOpenRate(String todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }

    public String getTodayCloseRate() {
        //(todayClosePrice-todayPrice)/todayPrice
        Float todayClosePrice =Float.parseFloat(getTodayClosePrice());
        Float todayOpenPrice =Float.parseFloat(getTodayOpenPrice());
        Float rate = (todayClosePrice-todayOpenPrice)/todayOpenPrice*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        todayCloseRate=decimalFormat.format(rate);
        return todayCloseRate;
    }

    public void setTodayCloseRate(String todayCloseRate) {
        this.todayCloseRate = todayCloseRate;
    }

    public String getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(String todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
    }

    public String getTomorrowOpenRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        Float tomorrowPrice =Float.parseFloat(getTomorrowOpenPrice());
        Float todayPrice =Float.parseFloat(getTodayOpenPrice());
        Float rate = (tomorrowPrice-todayPrice)/todayPrice*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        tomorrowOpenRate=decimalFormat.format(rate);
        return tomorrowOpenRate;
    }

    public void setTomorrowOpenRate(String tomorrowOpenRate) {
        this.tomorrowOpenRate = tomorrowOpenRate;
    }

    public String getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(String tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
    }

    public String getTomorrowCloseRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        Float tomorrowPrice =Float.parseFloat(getTomorrowClosePrice());
        Float todayPrice =Float.parseFloat(getTodayOpenPrice());
        Float rate = (tomorrowPrice-todayPrice)/todayPrice*100;
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        tomorrowCloseRate=decimalFormat.format(rate);
        return tomorrowCloseRate;
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
        sb.append(code).append(name).append(",昨收:").append(yesterdayPrice).append("<br>");
        return sb;
    }
    public StringBuilder toOpen(StringBuilder sb){
        sb.append(code).append(name).append(",竞价:").append(getTodayOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toOpenTomorrow(StringBuilder sb){
        sb.append(code).append(name).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toClose(StringBuilder sb){
        sb.append(code).append(name).append(",开盘:").append(getTodayOpenRate())
                .append(",收盘:").append(getTodayCloseRate()).append("<br>");
        return sb;
    }
    public StringBuilder toCloseTomorrow(StringBuilder sb){
        sb.append(code).append(name).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).append("<br>");
        return sb;
    }
}
