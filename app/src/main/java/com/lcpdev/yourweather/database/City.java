package com.lcpdev.yourweather.database;

import org.litepal.crud.DataSupport;


/**
 * Created by LCP on 2017/1/16.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class City extends DataSupport {
    private int id;
    private String cityName; //市的名字
    private int cityCode;    //市的代号
    private int provinceId;  //记录市所属的省ID

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
