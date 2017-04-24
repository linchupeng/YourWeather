package com.lcpdev.yourweather.gson;


/**
 * Created by LCP on 2017/1/24.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

/**
  * aqi 表示aqi指数 
  * pm25 表示pm2.5指数
  * qlty 表示空气质量
  */
public class Aqi {
    public AqiCity city;

  public class AqiCity {
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
