package com.example.stockdemo.dao;

import com.example.stockdemo.domain.DownStock;
import com.example.stockdemo.domain.DownStockAverage;
import com.example.stockdemo.domain.Temperature;
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
public interface DownStockAverageRepository extends JpaRepository<DownStockAverage,Long> {
    List<DownStockAverage> findAll();
    List<DownStockAverage> findByDayFormat(String dayFormat);
    DownStockAverage save(DownStockAverage downStock);
    @Query(value=" SELECT * FROM down_stock_average WHERE  day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<DownStockAverage> close(String start, String end);

}
