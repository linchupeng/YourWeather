package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linchupeng on 2017/1/24.
 * github:https://github.com/linchupeng/YourWeather
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
