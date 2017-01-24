package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linchupeng on 2017/1/24.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    private class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
