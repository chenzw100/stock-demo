package com.example.stockdemo.domain;

import com.example.stockdemo.enums.NumberEnum;
import com.example.stockdemo.utils.MyUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 股市看三日富可敌国，股市看两日稳定复利。
 * 昨天，今天，明天
 * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 *
 * columnDefinition="decimal(10,2) comment '提现金额'"
 */
@Entity(name="down_stock_average")
public class DownStockAverage {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    private Date created;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT NULL COMMENT '1强势'")
    private Integer stockType=1;

    @Column(nullable = true)
    private int todayOpenRate;
    @Column(nullable = true)
    private int todayCloseRate;
    @Column(nullable = true)
    private int tomorrowOpenRate;
    @Column(nullable = true)
    private int tomorrowCloseRate;

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

    public Integer getStockType() {
        return stockType;
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

    public int getTodayOpenRate() {
        return todayOpenRate;
    }

    public void setTodayOpenRate(int todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }

    public int getTodayCloseRate() {
        return todayCloseRate;
    }

    public void setTodayCloseRate(int todayCloseRate) {
        this.todayCloseRate = todayCloseRate;
    }

    public int getTomorrowOpenRate() {
        return tomorrowOpenRate;
    }

    public void setTomorrowOpenRate(int tomorrowOpenRate) {
        this.tomorrowOpenRate = tomorrowOpenRate;
    }

    public int getTomorrowCloseRate() {
        return tomorrowCloseRate;
    }

    public void setTomorrowCloseRate(int tomorrowCloseRate) {
        this.tomorrowCloseRate = tomorrowCloseRate;
    }
}
