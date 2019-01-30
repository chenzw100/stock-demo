package com.example.stockdemo.dao;

import com.example.stockdemo.domain.FiveTgbStock;
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
public interface FiveTgbStockRepository extends JpaRepository<FiveTgbStock,Long> {
    List<FiveTgbStock> findAll();
    List<FiveTgbStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<FiveTgbStock> findByDayFormatOrderByHotSort(String dayFormat);
    List<FiveTgbStock> findByDayFormatOrderByHotSevenDesc(String dayFormat);
    List<FiveTgbStock> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    List<FiveTgbStock> findByDayFormatOrderByOpenBidRate(String dayFormat);

    FiveTgbStock save(FiveTgbStock tgbStock);


}
