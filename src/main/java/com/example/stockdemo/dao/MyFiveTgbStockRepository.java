package com.example.stockdemo.dao;

import com.example.stockdemo.domain.FiveTgbStock;
import com.example.stockdemo.domain.MyFiveTgbStock;
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
public interface MyFiveTgbStockRepository extends JpaRepository<MyFiveTgbStock,Long> {
    List<MyFiveTgbStock> findAll();
    MyFiveTgbStock findByCodeAndDayFormat(String code, String dayFormat);
    List<MyFiveTgbStock> findByDayFormatOrderByHotSort(String dayFormat);
    List<MyFiveTgbStock> findByDayFormatOrderByHotSevenDesc(String dayFormat);
    List<MyFiveTgbStock> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    List<MyFiveTgbStock> findByDayFormatOrderByOpenBidRate(String dayFormat);
    MyFiveTgbStock save(MyFiveTgbStock tgbStock);
    @Query(value="SELECT * from my_five_tgb_stock WHERE day_format BETWEEN ?1 AND ?2  GROUP BY code", nativeQuery = true)
    public List<MyFiveTgbStock> fiveStatistic(String start , String end);


}
