package com.example.stockdemo.dao;

import com.example.stockdemo.domain.MyStock;
import com.example.stockdemo.domain.TgbStock;
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
public interface TgbStockRepository extends JpaRepository<TgbStock,Long> {
    List<TgbStock> findAll();
    List<TgbStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<TgbStock> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    List<TgbStock> findByDayFormatOrderByStockType(String dayFormat);
    TgbStock save(TgbStock tgbStock);

}
