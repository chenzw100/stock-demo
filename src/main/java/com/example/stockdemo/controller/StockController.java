package com.example.stockdemo.controller;

import com.example.stockdemo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@RestController
public class StockController {
    @Autowired
    private StockService stockService;

    @RequestMapping("/taoguba")
    public String taoguba() throws IOException {
        return stockService.taoguba();
    }
}
