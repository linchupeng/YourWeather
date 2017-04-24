package com.lcpdev.yourweather.model;

import android.util.Log;

/**
 * Created by linchupeng on 2017/2/19.
 * github:https://github.com/linchupeng/YourWeather
 */

public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    /**
     * 该静态常量的值用来控制你想打印的日志等级；
     * 比如当前LEVEL的值为常量1（VERBOSE），那么你以上5个日志等级都是可以打印的；
     * 假如当前LEVEL的值为常量2（DEBUG），那么你只能打印从DEBUG（2）到ERROR（5）之间的日志信息；
     * 假如你要是不想让日志信息打印出现，那么将LEVEL的值置为NOTHING即可。
     */
    public static final int LEVEL = NOTHING;

    // 调用Log.v()方法打印日志
    public static void v(String tag, String msg){
        if(LEVEL <= VERBOSE){
            /**
             * 加了一个日志信息值的判断，为了防止参数msg的值为null情况的出现
             */
            if(msg != null){
                Log.v(tag, msg);
            }else {
                Log.v(tag, "is NULL");
            }
        }
    }

    // 调用Log.d()方法打印日志
    public static void d(String tag, String msg){
        if(LEVEL <= VERBOSE){
            if(msg != null){
                Log.d(tag, msg);
            }else {
                Log.d(tag, "is NULL");
            }
        }
    }

    // 调用Log.i()方法打印日志
    public static void i(String tag, String msg){
        if(LEVEL <= VERBOSE){
            if(msg != null){
                Log.i(tag, msg);
            }else {
                Log.i(tag, "is NULL");
            }
        }
    }

    // 调用Log.w()方法打印日志
    public static void w(String tag, String msg){
        if(LEVEL <= VERBOSE){
            if(msg != null){
                Log.w(tag, msg);
            }else {
                Log.w(tag, "is NULL");
            }
        }
    }

    // 调用Log.e()方法打印日志
    public static void e(String tag, String msg){
        if(LEVEL <= VERBOSE){
            if(msg != null){
                Log.e(tag, msg);
            }else {
                Log.e(tag, "is NULL");
            }
        }
    }
}
