package com.lcpdev.yourweather.model;

import android.icu.text.LocaleDisplayNames;
import android.util.Log;

/**
 * Created by LinChuPeng on 2017/2/9.
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class Common {
    public static final String CITYIDS="北京CN101010100上海CN101020100天津CN101030100重庆CN101040100漳州CN101230601";
/**
 * 通过城市的名称获取对应的城市id，如果不存在则返回 null
 * @return weatherId;
 */

    public static String getCityIdByName(String cityName){
        String weatherId = null;
        int  startIndex = Common.CITYIDS.indexOf(cityName)+ cityName.length() ;// 开始截取的位置

        if (startIndex == -1) {
            return "CN101230601";
        }
        weatherId = Common.CITYIDS.substring(startIndex, startIndex + 11);
//        Log.d("CityName",weatherId);
        return weatherId;

    }

}
