package com.example.stockdemo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by chenzw on 2018/10/16.
 */
public class MyUtils {
    public static final long eight_hour = 8*60*60*1000;
    public static final long hour24 = 24*60*60*1000;
    public static Date getCurrentDate(){
        Date date = new Date();
        date.setTime(date.getTime()+eight_hour);
        return date;

    }
    public static Date getYesterdayDate(){
        Date date = new Date();
        date.setTime(date.getTime()-hour24);
        return date;

    }
    public static int getCentBySinaPriceStr(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue();
    }
    public static String getYuanByCent(int cent){
        return new BigDecimal(cent).setScale(2, RoundingMode.HALF_UP).toString();
        /*Double faultRate = Double.parseDouble(sinaPriceStr);
        BigDecimal a = BigDecimal.valueOf(faultRate);
        BigDecimal b =a.setScale(2, RoundingMode.HALF_UP);//保留两位小数；
        System.out.println("结果是"+b);
        //下面将结果转化成百分比
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        System.out.println(percent.format(b.doubleValue()));
        */

    }

    public static BigDecimal getIncreaseRate(int increase,int base){
        if(base==0){
            return new BigDecimal(0);
        }
        return new BigDecimal(increase-base).divide(new BigDecimal(base), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));


    }

    public static void main(String[] args) {
        System.out.println(MyUtils.getIncreaseRate(1100,1009));
    }
}
