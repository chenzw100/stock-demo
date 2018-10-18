package com.example.stockdemo.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by chenzw on 2018/10/18.
 */
public class WeiboMarketStockService extends MarketStockService {
    @Autowired
    private SinaService sinaService;
    @Override
    public Map getHopStock() {
        return sinaService.getHop24Stock();
    }
}
