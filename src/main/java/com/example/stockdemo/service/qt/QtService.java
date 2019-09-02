package com.example.stockdemo.service.qt;

import com.example.stockdemo.service.base.BaseService;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class QtService extends BaseService{
    static String url ="http://qt.gtimg.cn/q=s_";
    public String getCurrentPrice(String code){
        String[] stockObj = getStock(code);
        if(stockObj.length<3){
            return "-1";
        }
        return stockObj[3];
    }
    public Integer currentTradeVal() {
        String[] stockObj = getStock("sh000001");
        if(stockObj.length<7){
            log.error( ":err=qt");
            return -1;
        }
        String str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    public String[] getStock(String code) {
        Object response = getRequest(url+code);
        String str = response.toString();
        String[] stockObj = str.split("~");
        return stockObj;
    }
}
