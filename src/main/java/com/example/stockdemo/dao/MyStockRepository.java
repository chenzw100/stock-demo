package com.example.stockdemo.dao;

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
public interface MyStockRepository extends JpaRepository<MyStock,Long> {
    List<MyStock> findAll();
    List<MyStock> findByCode(String code);
    List<MyStock> findByCodeAndDayFormat(String code,String dayFormat);
    List<MyStock> findByDayFormatOrderByOpenBidRate(String dayFormat);
    MyStock save(MyStock myStock);

}
