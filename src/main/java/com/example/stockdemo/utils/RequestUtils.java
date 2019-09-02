package com.example.stockdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * Created by laikui on 2019/9/2.
 */
public class RequestUtils {
    @Autowired
    private RestTemplate restTemplate;
    public Object request(String url){
        Object response =null;
        try {
            response =  restTemplate.getForObject(url, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
