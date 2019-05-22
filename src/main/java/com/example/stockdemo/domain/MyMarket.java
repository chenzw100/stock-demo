package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by chenzw on 2018/10/22.
 */
@Entity(name="my_market")
public class MyMarket {
    @Id
    @GeneratedValue
   private long id;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String dayFormat;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private int yesterdayShow;
    @Column(nullable = false)
    private int continueShow;
    @Column(nullable = false)
    private int brokenShow;
    @Column(nullable = false)
    private int temperature;
    @Column(nullable = false)
    private int raiseUp;
    @Column(nullable = false)
    private int downUp;
    @Column(nullable = false)
    private int broken;
    @Column(nullable = false)
    private int raise;
    @Column(nullable = false)
    private int down;
    @Column(nullable = false)
    private int type;
    @Column(nullable = false)
    private int tradeVal;
    @Column(nullable = false)
    private int strongDowns;
    @Column(nullable = false)
    private int continueCount;

    public int getContinueCount() {
        return continueCount;
    }

    public void setContinueCount(int continueCount) {
        this.continueCount = continueCount;
    }

    public int getStrongDowns() {
        return strongDowns;
    }

    public void setStrongDowns(int strongDowns) {
        this.strongDowns = strongDowns;
    }

    public int getTradeVal() {
        return tradeVal;
    }

    public void setTradeVal(int tradeVal) {
        this.tradeVal = tradeVal;
    }

    public MyMarket(){
    }
    public MyMarket(int type){
        this.type = type;
        this.created = MyUtils.getCurrentDate();
        this.dayFormat= MyUtils.getDayFormat();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getYesterdayShow() {
        return yesterdayShow;
    }

    public void setYesterdayShow(int yesterdayShow) {
        this.yesterdayShow = yesterdayShow;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getRaiseUp() {
        return raiseUp;
    }

    public void setRaiseUp(int raiseUp) {
        this.raiseUp = raiseUp;
    }

    public int getDownUp() {
        return downUp;
    }

    public void setDownUp(int downUp) {
        this.downUp = downUp;
    }

    public int getBroken() {
        return broken;
    }

    public void setBroken(int broken) {
        this.broken = broken;
    }

    public int getRaise() {
        return raise;
    }

    public void setRaise(int raise) {
        this.raise = raise;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContinueShow() {
        return continueShow;
    }

    public void setContinueShow(int continueShow) {
        this.continueShow = continueShow;
    }

    public int getBrokenShow() {
        return brokenShow;
    }

    public void setBrokenShow(int brokenShow) {
        this.brokenShow = brokenShow;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        String dateStr = DateFormatUtils.format(getCreated(), "MM-dd HH:mm");
        sb.append(dateStr+"=> [昨:").append(MyUtils.getYuanByCent(getYesterdayShow()));
        sb.append("] [连:").append(getContinueShow());
        sb.append("] [温:").append(getTemperature());
        sb.append("] [涨:").append(getRaiseUp()).append(", 跌:").append(getDownUp()).append(", 炸:").append(getBroken());
        sb.append("] [涨:").append(getRaise()).append(", 跌:").append(getDown()).append("] [额:").append(getTradeVal()).append("亿]");
        if(dateStr.substring(6,8).equals("15")){
            sb.append(" [负:").append(getStrongDowns()).append("] [正:").append(getContinueCount()).append("]<br>");
        }else {
            sb.append("<br>");
        }

        return sb.toString();
    }
}
