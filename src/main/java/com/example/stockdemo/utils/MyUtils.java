package com.example.stockdemo.utils;

import java.util.Date;

/**
 * Created by chenzw on 2018/10/16.
 */
public class MyUtils {
    public static final long eight_hour = 1*60*60*1000;
    public static Date getCurrentDate(){
        Date date = new Date();
        date.setTime(date.getTime()+eight_hour);
        return date;

    }
}
