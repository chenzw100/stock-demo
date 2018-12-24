package com.example.stockdemo.dao;

import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.domain.XGBStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 * User user=new User();
 userRepository.findAll();
 userRepository.findOne(1l);
 userRepository.save(user);
 userRepository.delete(user);
 userRepository.count();
 userRepository.exists(1l);
 */
public interface XgbStockRepository extends JpaRepository<XGBStock,Long> {
    List<XGBStock> findAll();
    List<XGBStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<XGBStock> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    List<XGBStock> findByDayFormatOrderByStockType(String dayFormat);
    XGBStock save(XGBStock xgbStock);

}
