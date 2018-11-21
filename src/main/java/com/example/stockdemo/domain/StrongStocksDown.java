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
    @Column(nullable = false,columnDefinition="varchar(50) COMMENT '前3'")
    private String desc;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String dayFormat;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
