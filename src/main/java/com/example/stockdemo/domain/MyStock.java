package com.example.stockdemo.domain;

import com.example.stockdemo.enums.NumberEnum;
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
@Entity(name="up_stock")
public class MyStock implements Comparable<MyStock>{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
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
    @Column(nullable = true,columnDefinition="varchar(10) COMMENT '连板'")
    private String continuous;
    @Column(nullable = true)
    private Integer openCount;
    @Column(nullable = true)
    private Integer oneFlag;
    @Column(nullable = true)
    private Integer hotSort;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT NULL COMMENT '1实时;2一天;3两者'")
    private Integer stockType;

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

    public Integer getStockType() {
        if(stockType == null){
            stockType=0;
        }
        return stockType;
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

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
        getOpenBidRate();
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
    static String strHead = "<span color='red'>";
    static String strTail = "</span>";
    public StringBuilder toChoice(StringBuilder sb){
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(stockType).append(strTail).append(",");
        }else {
            sb.append("热门:").append(stockType).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",昨收:").append(MyUtils.getYuanByCent(getYesterdayClosePrice())).append("<br>");
        return sb;
    }
    public StringBuilder toOpen(StringBuilder sb){
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(stockType).append(strTail).append(",");
        }else {
            sb.append("热门:").append(stockType).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",竞价:").append(getTodayOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toOpenTomorrow(StringBuilder sb){
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(stockType).append(strTail).append(",");
        }else {
            sb.append("热门:").append(stockType).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",开板:").append(openCount).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append("<br>");
        return sb;
    }
    public StringBuilder toClose(StringBuilder sb){
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(stockType).append(strTail).append(",");
        }else {
            sb.append("热门:").append(stockType).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",开板:").append(openCount).append(",开盘:").append(getTodayOpenRate())
                .append(",收盘:").append(getTodayCloseRate()).append("<br>");
        return sb;
    }
    public StringBuilder toCloseTomorrow(StringBuilder sb){
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(stockType).append(strTail).append(",");
        }else {
            sb.append("热门:").append(stockType).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",开板:").append(openCount).append(",今天:").append(getTodayOpenRate())
                .append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).append("<br>");
        return sb;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(stockType== NumberEnum.StockType.COMMON.getCode()){
            sb.append(strHead).append("热门:").append(NumberEnum.StockType.getStockType(getStockType())).append(strTail).append(",");
        }else {
            sb.append("热门:").append(NumberEnum.StockType.getStockType(getStockType())).append(",");;
        }
        sb.append(code).append(name).append(",连板:").append(continuous).append(",一字").append(oneFlag).append(",开板:").append(openCount).append(",热排").append(hotSort).append(",竞价:").append(getTodayOpenRate())
        .append(",收盘:").append(getTodayCloseRate()).append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).append(plateName).append("<br>");
        return sb.toString();
    }


    @Override
    public int compareTo(MyStock o) {
        return o.openBidRate-this.openBidRate;
    }
}
