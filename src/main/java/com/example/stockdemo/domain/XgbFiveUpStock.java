package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;

import javax.persistence.*;
import java.util.Date;

/**
 *  * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 *  * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 */
@Entity(name="xgb_five_up_stock")
public class XgbFiveUpStock implements Comparable<XgbFiveUpStock>{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private Integer openCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continueBoardCount;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开盘竞价'")
    private Integer openBidRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最高'")
    private Integer fiveHighPrice;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最低'")
    private Integer fiveLowPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '出现次数'")
    private Integer showCount;
    @Column(nullable = false)
    private Date created;
    @Transient
    private int downRate;
    @Transient
    private String price;
    @Transient
    private String fiveHighRate;
    @Transient
    private String fiveLowRate;

    public Integer getShowCount() {
        if(showCount==null){
            showCount=0;
        }
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.dayFormat=MyUtils.getDayFormat(created);
        this.created = created;
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

    public Integer getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(Integer yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        yesterdayClosePrice=MyUtils.getCentBySinaPriceStr(price);
        this.price = price;
    }

    public int getDownRate() {
        return downRate;
    }

    public void setDownRate(int downRate) {
        this.downRate = downRate;
    }

    public Integer getContinueBoardCount() {
        if(continueBoardCount == null){
            continueBoardCount =-1;
        }
        return continueBoardCount;
    }

    public void setContinueBoardCount(Integer continueBoardCount) {
        this.continueBoardCount = continueBoardCount;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
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

    public Integer getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(Integer todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public Integer getOpenBidRate() {
        openBidRate =MyUtils.getIncreaseRateCent(getTodayOpenPrice(),getYesterdayClosePrice()).intValue();
        return openBidRate;
    }

    public void setOpenBidRate(Integer openBidRate) {
        this.openBidRate = openBidRate;
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

    public int compareTo(XgbFiveUpStock o) {
          // TODO Auto-generated method stub
          return this.downRate-o.downRate;
     }
    public DownStock coverDownStock(){
        DownStock downStock = new DownStock(getCode(),getName());
        downStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(getPrice()));
        downStock.setDownRate(downRate);
        return downStock;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(name).append("出现:").append(showCount).append(",竞价:").append(getOpenBidRate()).append(",最高:").append(getFiveHighRate()).append(",最低:").append(getFiveLowRate())
                .append("<br>").append(name).append(":").append(getTodayOpenPrice()).append(plateName).append("<br>");
        return sb.toString();
    }
}
