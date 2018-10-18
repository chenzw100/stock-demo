package com.example.stockdemo.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by chenzw on 2018/10/18.
 */
public class TaogubaMarketStockService extends MarketStockService {
    @Autowired
    private TgbService tgbService;
    public  Map getHopStock(){
        return tgbService.getHopAllStock();
    }
}
