package com.example.stockdemo.dao;

import com.example.stockdemo.domain.DownStockAverage;
import com.example.stockdemo.domain.SpaceHeight;
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
public interface SpaceHeightRepository extends JpaRepository<SpaceHeight,Long> {
    List<SpaceHeight> findAll();
    List<SpaceHeight> findByDayFormat(String dayFormat);
    SpaceHeight save(SpaceHeight downStock);
    @Query(value=" SELECT * FROM space_height WHERE  day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<SpaceHeight> close(String start, String end);

}
