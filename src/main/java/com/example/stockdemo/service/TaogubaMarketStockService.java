package com.example.stockdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by chenzw on 2018/10/18.
 */
@Service("tgbMarketStockService")
public class TaogubaMarketStockService extends MarketStockService {
    @Autowired
    private TgbService tgbService;
    public  Map getHopStock(){
        return tgbService.getHopAllStock();
    }
}
