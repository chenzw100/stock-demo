package com.example.stockdemo.dao;

import com.example.stockdemo.domain.DownStock;
import com.example.stockdemo.domain.MyStock;
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
public interface DownStockRepository extends JpaRepository<DownStock,Long> {
    List<DownStock> findAll();
    List<DownStock> findByCode(String code);
    List<DownStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<DownStock> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    DownStock save(DownStock downStock);

}
