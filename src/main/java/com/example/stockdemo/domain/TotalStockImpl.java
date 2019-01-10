package com.example.stockdemo.domain;

/**
 * Created by laikui on 2019/1/10.
 */
public class TotalStockImpl {
    private TotalStock totalStock;
    public TotalStockImpl(TotalStock totalStock) {
        this.totalStock = totalStock;
    }

    @Override
    public String toString() {
        return totalStock.getCode()+totalStock.getName()+":"+totalStock.getTotalCount()+"<br>";
    }
}
