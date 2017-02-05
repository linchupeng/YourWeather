package com.lcpdev.yourweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linchupeng on 2017/1/27.
 * github:https://github.com/linchupeng/YourWeather
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;
    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

   public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
