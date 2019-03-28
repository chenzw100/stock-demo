package com.example.stockdemo.dao;

import com.example.stockdemo.domain.MyTotalStock;
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
 SELECT * FROM temperature WHERE type=1 ORDER BY id DESC LIMIT 5
 SELECT * FROM temperature WHERE type=3 ORDER BY id DESC LIMIT 5
 WHERE day_format BETWEEN ?1 AND ?2
 SELECT * FROM temperature WHERE type=1 and day_format BETWEEN '20190321' AND '20190328' ORDER BY id DESC;
 */
public interface TemperatureRepository extends JpaRepository<Temperature,Long> {
    List<Temperature> findAll();
    List<Temperature> findByDayFormat(String dayFormat);
    List<Temperature> findByDayFormatOrderById(String dayFormat);
    List<Temperature> findByDayFormatAndType(String dayFormat,int type);
    Temperature save(Temperature temperature);
    @Query(value=" SELECT * FROM temperature WHERE type=1 and day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<Temperature> open(String start, String end);
    @Query(value=" SELECT * FROM temperature WHERE type=2 and day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<Temperature> close(String start, String end);

}
