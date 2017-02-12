package com.lcpdev.yourweather.database;

import org.litepal.crud.DataSupport;


/**
 * Created by LCP on 2017/1/16.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */
public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;//县对应ID
    private int cityId;//县当前所属的市ID

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
