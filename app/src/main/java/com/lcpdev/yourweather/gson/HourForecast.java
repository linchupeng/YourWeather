package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linchupeng on 2017/2/12.
 * github:https://github.com/linchupeng/YourWeather
 */

	/**
	 * 未来小时的天气信息
	 */
public class HourForecast {
    public String date;
    public String hum;
    public String tmp;
    @SerializedName("wind")  // 风向
    public Wind wind;

    public class Wind {
        public String spd; // 风速
    }
}

