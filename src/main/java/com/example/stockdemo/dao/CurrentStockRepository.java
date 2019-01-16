package com.example.stockdemo.dao;

import com.example.stockdemo.domain.CurrentStock;
import com.example.stockdemo.domain.TgbStock;
import com.example.stockdemo.domain.TotalStock;
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
public interface CurrentStockRepository extends JpaRepository<CurrentStock,Long> {
    TgbStock save(CurrentStock tgbStock);
    @Query(value="SELECT * FROM ( SELECT *, COUNT(id) as total_count from tgb_stock WHERE day_format BETWEEN ?1 AND ?2  GROUP BY code) as temp WHERE temp.total_count>36 ORDER BY total_count DESC ", nativeQuery = true)
    public List<TgbStock> totalCount(String start, String end);

    @Query(value="SELECT * FROM ( SELECT code, name, COUNT(id) as total_count from tgb_stock WHERE day_format BETWEEN ?1 AND ?2  GROUP BY code) as temp WHERE temp.total_count>36 ORDER BY total_count DESC ", nativeQuery = true)
    public List<TotalStock> stockInfo(String start, String end);

}
