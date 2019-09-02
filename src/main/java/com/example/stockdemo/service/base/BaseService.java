package com.example.stockdemo.service.base;

import com.example.stockdemo.utils.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class BaseService {
    public Log log = LogFactory.getLog(BaseService.class);
    @Autowired
    RequestUtils requestUtils;

    public Object getRequest(String url){
        return requestUtils.request(url);
    }

}
