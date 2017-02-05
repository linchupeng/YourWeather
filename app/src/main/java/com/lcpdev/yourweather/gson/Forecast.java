package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linchupeng on 2017/1/27.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
