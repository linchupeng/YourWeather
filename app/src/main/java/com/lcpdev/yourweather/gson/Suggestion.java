package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;


/**
 * Created by LCP on 2017/1/27.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class Suggestion {
    @SerializedName("air")
    public Air air;
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("flu")
    public Influenza flu;
    public Dress drsg;
    public class Air {
        @SerializedName("brf")
        public String info;
        @SerializedName("txt")
        public String infotxt;
    }
    public class Comfort {
        @SerializedName("brf")
        public String info;
        @SerializedName("txt")
        public String infotxt;
    }
    public class Influenza {
        @SerializedName("brf")
        public String info;
        @SerializedName("txt")
        public String infotxt;
    }
    public class Dress {
        @SerializedName("brf")
        public String info;
        @SerializedName("txt")
        public String infotxt;
    }



}
