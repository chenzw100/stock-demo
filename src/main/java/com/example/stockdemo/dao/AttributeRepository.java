package com.example.stockdemo.dao;

import com.example.stockdemo.domain.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public interface AttributeRepository extends JpaRepository<Attribute,Long> {
    @Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
            countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
            nativeQuery = true)
    List<Attribute> findAll();
    Attribute findByName(String name);
    Attribute save(Attribute attribute);

}
