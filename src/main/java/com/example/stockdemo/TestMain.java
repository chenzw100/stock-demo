package com.example.stockdemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by laikui on 2018/10/16.
 */
public class TestMain {

    public static void main(String[] args) {
/*
        Float yesterday =Float.parseFloat("9.920");
        Float opening =Float.parseFloat("8.490");
        Float rate = (opening-yesterday)/yesterday*100;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        System.out.println(rate+":"+ decimalFormat.format(rate));*/
       /* String response = "{\"code\":200,\"data\":{\"fields\":[\"symbol\",\"title\",\"reason\",\"cur_price\",\"px_change_rate\",\"blockade_ratio\",\"turnover_ratio\",\"circulation_value\",\"total_value\",\"first_raise_time\",\"last_raise_time\",\"open_count\",\"continue_board_count\",\"bool_new_stock\",\"block_intensity\",\"plates_reason\"],\"items\":[[\"002263.SZ\",\"*ST东南\",\"此前连续12日跌停，集团质押遭平仓\",1.65,5.095541401273875,0.000026086584782119254,3.153146300328675,3099294165,3099294165,1541385159,1541398245,7,1,false,0.010434633912847702,[{\"plate_id\":24898553,\"plate_name\":\"ST股\"}]],[\"000622.SZ\",\"恒立实业\",\"国内最早从事汽车空调零部件行业的企业；民企；第一大股东为中国华阳投资控股有限公司\",5.53,9.940357852882704,0.09250769473174265,0.6349148452822734,2351499780,2351499780,1541381400,1541381400,0,8,false,37.003077892697064,[{\"plate_id\":2843278,\"plate_name\":\"壳公司\"},{\"plate_id\":22082034,\"plate_name\":\"汽车零部件\"}]]]}}";
    JSONArray closeLimitUp = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
    for(int i=0;i<closeLimitUp.size();i++){
            JSONArray jsonArray =  closeLimitUp.getJSONArray(i);
            System.out.println(jsonArray.toArray()[0].toString().substring(0,6)+":"+jsonArray.toArray()[12]);
    }*/
        System.out.println("sh123456".substring(2,8));
    }
}
