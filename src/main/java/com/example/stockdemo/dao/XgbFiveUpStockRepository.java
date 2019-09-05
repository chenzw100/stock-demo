package com.example.stockdemo.dao;

import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.domain.XGBStock;
import com.example.stockdemo.domain.XgbFiveUpStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
public interface XgbFiveUpStockRepository extends JpaRepository<XgbFiveUpStock,Long> {
    List<XgbFiveUpStock> findAll();
    List<XgbFiveUpStock> findByCodeAndDayFormat(String code, String dayFormat);
    List<XgbFiveUpStock> findByDayFormatAndContinueBoardCountGreaterThan(String dayFormat, int min);
    XgbFiveUpStock save(XgbFiveUpStock xgbStock);
    List<XgbFiveUpStock> findByDayFormatOrderByContinueBoardCountDesc(String dayFormat);
    @Query(value="SELECT * from xgb_five_up_stock WHERE day_format BETWEEN ?1 AND ?2  GROUP BY code", nativeQuery = true)
    public List<XgbFiveUpStock> fiveStatistic(String start ,String end);

}
