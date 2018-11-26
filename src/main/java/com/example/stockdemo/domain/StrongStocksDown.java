package com.example.stockdemo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by chen on 2018/11/21.
 */
@Entity(name="stocks_down")
public class StrongStocksDown {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer downCount;
    @Column(nullable = false)
    private Integer type;//1:强势；2：炸板
    @Column(nullable = false,columnDefinition="varchar(50) COMMENT '前3'")
    private String stockDesc;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String dayFormat;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public String getStockDesc() {
        return stockDesc;
    }

    public void setStockDesc(String stockDesc) {
        this.stockDesc = stockDesc;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getDayFormat()).append("=>type:").append(type).append(",跌数:").append(downCount).append(",前三:").append(stockDesc);
        return sb.toString();
    }
}
