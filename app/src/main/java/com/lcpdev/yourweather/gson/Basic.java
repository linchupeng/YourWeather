package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;


/**
 * Created by LCP on 2017/1/24.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */
public class Basic {
    @SerializedName("city") //城市名
    public String cityName;
    @SerializedName("id") //城市对应的天气ID
    public String weatherId;
    public Update update;

    //天气更新时间
    private class Update {
        @SerializedName("loc") //loc 表示天气的更新时间
        public String updateTime;
    }
}
