package com.lcpdev.yourweather;

/**
 * Created by linchupeng on 2017/2/4.
 * github:https://github.com/linchupeng/YourWeather
 */

public class DoubleClickExit {
    /**
     * 双击退出  间隔为1s
     */
    public static long mLastClick = 0L;
    private static final int THRESHOLD = 2000;// 1000ms

    public static boolean check() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD;
        mLastClick = now;
        return b;
    }
}
