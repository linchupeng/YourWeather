package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by linchupeng on 2017/1/27.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Weather {
    public String status;
    public Aqi aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastsList;
}
