package com.example.stockdemo.dao;

import com.example.stockdemo.domain.Attribute;
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
    MyStock findByCode(String code);
    MyStock findByCodeAndDayFormat(String code,String dayFormat);
    MyStock save(MyStock myStock);

}
