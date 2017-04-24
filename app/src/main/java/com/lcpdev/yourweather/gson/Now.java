package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;


/**
 * Created by LCP on 2017/1/24.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

    /**
     *当前天气温度和情况
     */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("code")
        public int code;
        @SerializedName("txt")
        public String info;
    }
}
