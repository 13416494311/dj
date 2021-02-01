package com.ruoyi.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;

import java.io.IOException;


/**
 * 百度地图
 * @author:万登枫
 * @date: 2021/1/26
 */
public class BaiduMapUtils {

    static String AK = "GP7XayOwlsGl9BhVFQDInjpOBgD3TUQT"; // 百度地图密钥
    static String URL = "http://api.map.baidu.com/geocoder"; // 百度地图密钥
    static int tryCatchTimes = 3; //未获取到时 重复次数

    public static void main(String[] args) {
        Double[] coordinate = getCoordinate("湛江市");
        System.out.println(coordinate[0]+"  "+coordinate[1]);//0:经度 1:纬度
    }


    /**
     * 根据城市名称查询所在经纬度
     * @param address 要查询的地址
     * 查询的地址
     * @return
     * @throws IOException
     */
    public static Double[] getCoordinate(String address)  {
        int time = 0;
        try{
            if (address != null && !"".equals(address)) {
                String param = String .format("address=%s&output=json&key=%s", address, AK);
                String json = HttpUtils.sendGet(URL,param);
                if (json != null && !"".equals(json)) {
                    JSONObject obj = JSONObject.parseObject(json);
                    if ("OK".equals(obj.getString("status"))) {
                        Double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng"); // 经度
                        Double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat"); // 纬度
                        return new Double[]{lng,lat};
                    }
                }
            }
        }catch (Exception e){
            time++;
            if(time <= tryCatchTimes){
                return getCoordinate(address);
            }
        }
        return null;
    }

}
