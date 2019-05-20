package com.example.stockdemo.dao;

import com.example.stockdemo.domain.MeStock;
import com.example.stockdemo.domain.MyFiveTgbStock;
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
public interface MeStockRepository extends JpaRepository<MeStock,Long> {
    List<MeStock> findAll();
    List<MeStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<MeStock> findByDayFormatOrderByOpenBidRate(String dayFormat);
    MeStock save(MeStock tgbStock);


}