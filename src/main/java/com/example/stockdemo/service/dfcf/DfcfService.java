package com.example.stockdemo.service.dfcf;

import com.example.stockdemo.service.base.BaseService;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfService extends BaseService {
    private static String current_Continue="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String current_Yesterday="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";

    public String currentContinueVal() {
        Object response =  getRequest(current_Continue);
        if(response==null){
            return "-1";
        }
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return "-1";
        }
        str =stockObj[5];
        return str;
    }
    public String currentYesterdayVal() {
        Object response =  getRequest(current_Yesterday);
        if(response==null){
            return "-1";
        }
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return "-1";
        }
        str =stockObj[5];
        return str;
    }
}
