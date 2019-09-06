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
 */
@Entity(name="five_tgb_stock")
public class FiveTgbStock implements Comparable<FiveTgbStock>{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热门排序'")
    private Integer hotSort;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '0未涨停;1涨停'")
    private Integer limitUp;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开盘竞价'")
    private Integer openBidRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日热搜'")
    private Integer hotValue;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '最近7日'")
    private Integer hotSeven;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continuous;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private Integer openCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '大于0开板'")
    private Integer oneFlag;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String remark;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '出现次数'")
    private Integer showCount;


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

    public Integer getShowCount() {
        if(showCount==null){
            showCount=0;
        }
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最高'")
    private Integer fiveHighPrice;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最低'")
    private Integer fiveLowPrice;
    @Transient
    private String fiveHighRate;
    @Transient
    private String fiveLowRate;

    public String getFiveHighRate() {
        return MyUtils.getIncreaseRate(getFiveHighPrice(),getTodayOpenPrice()).toString();
    }

    public void setFiveHighRate(String fiveHighRate) {
        this.fiveHighRate = fiveHighRate;
    }

    public String getFiveLowRate() {
        return MyUtils.getIncreaseRate(getFiveLowPrice(),getTodayOpenPrice()).toString();
    }

    public void setFiveLowRate(String fiveLowRate) {
        this.fiveLowRate = fiveLowRate;
    }

    public Integer getFiveHighPrice() {
        return fiveHighPrice;
    }

    public void setFiveHighPrice(Integer fiveHighPrice) {
        this.fiveHighPrice = fiveHighPrice;
    }

    public Integer getFiveLowPrice() {
        return fiveLowPrice;
    }

    public void setFiveLowPrice(Integer fiveLowPrice) {
        this.fiveLowPrice = fiveLowPrice;
    }

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


    public Integer getLimitUp() {
        return limitUp;
    }

    public void setLimitUp(Integer limitUp) {
        this.limitUp = limitUp;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getHotSort() {
        return hotSort;
    }

    public void setHotSort(Integer hotSort) {
        this.hotSort = hotSort;
    }

    public Integer getOneFlag() {
        return oneFlag;
    }

    public void setOneFlag(Integer oneFlag) {
        this.oneFlag = oneFlag;
    }



    public Integer getContinuous() {
        return continuous;
    }

    public void setContinuous(Integer continuous) {
        this.continuous = continuous;
    }

    public FiveTgbStock(){
    }
    public FiveTgbStock(String code, String name){
        this.code =code;
        this.name = name;
        this.sinaUrl="https://hq.sinajs.cn/list="+code;
        this.yesterdayClosePrice=10;
        this.todayOpenPrice=10;
        this.todayClosePrice=10;
        this.tomorrowOpenPrice=10;
        this.tomorrowClosePrice=10;
        this.fiveHighPrice=10;
        this.fiveLowPrice=10;
        this.oneFlag=-1;
        this.openCount=-1;
        this.continuous=-1;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    public Integer getHotSeven() {
        return hotSeven;
    }

    public void setHotSeven(Integer hotSeven) {
        this.hotSeven = hotSeven;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
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
        sb.append(code).append(name).append("[出现:").append(showCount).append("热值:").append(hotValue).
                append("七日:").append(hotSeven).append(",连板:").append(continuous).append("]竞价:").append(getTodayOpenRate()).append(",收盘:").append(getTodayCloseRate()).append(",明天:").append(getTomorrowOpenRate()).
                append(":").append(getTomorrowCloseRate()).append(name).append("<br>").append(plateName).append("<br>");
        return sb.toString();
    }
    public MeStock toMeStock(){
        MeStock stock = new MeStock();
        stock.setName(name);
        stock.setYesterdayClosePrice(yesterdayClosePrice);
        stock.setCode(code);
        stock.setContinuous(continuous);
        stock.setCreated(created);
        stock.setDayFormat(dayFormat);
        stock.setOpenBidRate(openBidRate);
        stock.setPlateName(plateName);
        stock.setRemark(remark);
        stock.setTodayClosePrice(todayClosePrice);
        stock.setTodayOpenPrice(todayOpenPrice);
        stock.setTomorrowClosePrice(tomorrowClosePrice);
        stock.setTomorrowOpenPrice(tomorrowOpenPrice);
        return stock;
    }


    @Override
    public int compareTo(FiveTgbStock o) {
        return o.openBidRate-this.openBidRate;
    }
}
