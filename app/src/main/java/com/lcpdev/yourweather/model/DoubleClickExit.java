package com.lcpdev.yourweather.model;

import android.os.Process;
import android.widget.Toast;

import com.lcpdev.yourweather.R;


/**
 * Created by linchupeng on 2017/2/9.
 * github:https://github.com/linchupeng/YourWeather
 */

public class DoubleClickExit {
    public static long exitTime = 0;
    private static final int THRESHOLD = 2000;// 1000ms
    public static void  Exit(){
//        if (System.currentTimeMillis()-exitTime>THRESHOLD){
//            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        }else {
//            finish();
//            System.exit(0);
//            Process.killProcess(Process.myPid());
//        }

    }
}
