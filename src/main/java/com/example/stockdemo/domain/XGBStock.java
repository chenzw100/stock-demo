package com.example.stockdemo.domain;

public class XGBStock {
    private String code;
    private String name;
    private Integer openCount;
    private Integer continueBoardCount;

    public Integer getContinueBoardCount() {
        if(continueBoardCount == null){
            continueBoardCount=-1;
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
}
