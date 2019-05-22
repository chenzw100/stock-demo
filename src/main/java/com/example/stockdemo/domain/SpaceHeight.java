package com.example.stockdemo.domain;

import com.example.stockdemo.utils.MyUtils;

import javax.persistence.*;
import java.util.Date;

@Entity(name="space_height")
public class SpaceHeight {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false)
    private Date created;

    @Column(nullable = false,columnDefinition="varchar(8)")
    private String firstCode;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String firstName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private int firstOpen;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private int firstContinue;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String firstPlate;

    private String secondCode;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String secondName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private int secondOpen;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private int secondContinue;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String secondPlate;



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

    public String getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getFirstOpen() {
        return firstOpen;
    }

    public void setFirstOpen(int firstOpen) {
        this.firstOpen = firstOpen;
    }

    public int getFirstContinue() {
        return firstContinue;
    }

    public void setFirstContinue(int firstContinue) {
        this.firstContinue = firstContinue;
    }

    public String getFirstPlate() {
        return firstPlate;
    }

    public void setFirstPlate(String firstPlate) {
        this.firstPlate = firstPlate;
    }

    public String getSecondCode() {
        return secondCode;
    }

    public void setSecondCode(String secondCode) {
        this.secondCode = secondCode;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getSecondOpen() {
        return secondOpen;
    }

    public void setSecondOpen(int secondOpen) {
        this.secondOpen = secondOpen;
    }

    public int getSecondContinue() {
        return secondContinue;
    }

    public void setSecondContinue(int secondContinue) {
        this.secondContinue = secondContinue;
    }

    public String getSecondPlate() {
        return secondPlate;
    }

    public void setSecondPlate(String secondPlate) {
        this.secondPlate = secondPlate;
    }
}
