package com.lcpdev.yourweather.gson;


/**
 * Created by LCP on 2017/1/24.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class Aqi {
    public AqiCity city;

  public class AqiCity {
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
