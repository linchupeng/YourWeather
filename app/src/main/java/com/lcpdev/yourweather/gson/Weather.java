package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LCP on 2017/1/27.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class Weather {
    public String status;
    public Aqi aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("hourly_forecast")
    public List<HourForecast> hourForecastList;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastsList;
}
