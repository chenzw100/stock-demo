package com.example.stockdemo.dao;

import com.example.stockdemo.domain.StrongStocksDown;
import com.example.stockdemo.domain.Temperature;
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
public interface StrongStocksDownRepository extends JpaRepository<StrongStocksDown,Long> {
    List<StrongStocksDown> findAll();
    List<StrongStocksDown> findByDayFormat(String dayFormat);
    StrongStocksDown save(StrongStocksDown strongStocksDown);

}
