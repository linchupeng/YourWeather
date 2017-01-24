package com.lcpdev.yourweather.gson;

/**
 * Created by linchupeng on 2017/1/24.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Aqi {
    public AqiCity city;

    private class AqiCity {
        public String aqi;
        public String pm25;
    }
}
