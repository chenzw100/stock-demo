package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 股市看三日富可敌国，股市看两日稳定复利。
 * 昨天，今天，明天
 * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 */
@Entity(name="me_stock")
public class MeStock {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;

    @Column(nullable = true,columnDefinition="varchar(30) COMMENT '板块'")
    private String plateName;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开盘竞价'")
    private Integer openBidRate;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continuous;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '竞价开盘简讯'")
    private String remark;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天开盘'")
    private Integer tomorrowOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天收盘'")
    private Integer tomorrowClosePrice;
    @Column(nullable = false)
    private Date created;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    private String todayOpenRate;
    @Transient
    private String todayCloseRate;
    @Transient
    private String tomorrowOpenRate;
    @Transient
    private String tomorrowCloseRate;
    @Transient
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Transient
    private String sinaUrl;



    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }




    public Integer getContinuous() {
        return continuous;
    }

    public void setContinuous(Integer continuous) {
        this.continuous = continuous;
    }

    public MeStock(){
    }
    public MeStock(String code, String name){
        this.code =code;
        this.name = name;
        this.sinaUrl="https://hq.sinajs.cn/list="+code;
        this.yesterdayClosePrice=10;
        this.todayOpenPrice=10;
        this.todayClosePrice=10;
        this.tomorrowOpenPrice=10;
        this.tomorrowClosePrice=10;

        this.continuous=-1;
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
        this.dayFormat=MyUtils.getDayFormat(created);
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

    public Integer getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(Integer yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public Integer getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(Integer todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
        getOpenBidRate();
    }

    public Integer getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(Integer todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
    }

    public Integer getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(Integer tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
    }

    public Integer getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(Integer tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
    }
    public Integer getOpenBidRate() {
        openBidRate =MyUtils.getIncreaseRateCent(getTodayOpenPrice(),getYesterdayClosePrice()).intValue();
        return openBidRate;
    }

    public void setOpenBidRate(Integer openBidRate) {
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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(name).append("[连板:").append(continuous).append("]竞价:").append(getTodayOpenRate()).append("【简讯:").append(remark).append("】,收盘:").append(getTodayCloseRate()).append(",明天:").append(getTomorrowOpenRate()).
                append(":").append(getTomorrowCloseRate()).append(plateName).append("<br>");
        return sb.toString();
    }


}