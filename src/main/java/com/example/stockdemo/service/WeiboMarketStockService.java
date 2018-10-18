package com.example.stockdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by chenzw on 2018/10/18.
 */
@Service("wbMarketStockService")
public class WeiboMarketStockService extends MarketStockService {
    @Autowired
    private SinaService sinaService;
    @Override
    public Map getHopStock() {
        return sinaService.getHop24Stock();
    }
}
