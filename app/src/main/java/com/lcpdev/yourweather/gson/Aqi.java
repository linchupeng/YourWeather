package com.lcpdev.yourweather.gson;

/**
 * Created by linchupeng on 2017/1/24.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Aqi {
    public AqiCity city;

  public class AqiCity {
        public String aqi;
        public String pm25;
    }
}
