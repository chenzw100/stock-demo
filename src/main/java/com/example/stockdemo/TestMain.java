package com.example.stockdemo;

import java.text.DecimalFormat;

/**
 * Created by laikui on 2018/10/16.
 */
public class TestMain {

    public static void main(String[] args) {

        Float yesterday =Float.parseFloat("9.920");
        Float opening =Float.parseFloat("8.490");
        Float rate = (opening-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        System.out.println(rate+":"+ decimalFormat.format(rate));
    }
}
