package com.lcpdev.yourweather;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.lcpdev.yourweather.model.DoubleClickExit.exitTime;

/**
 * Created by 林楚鹏 on 2017/1/17.
 */

public class BaseActivity extends AppCompatActivity{

    /**
     * 设置状态栏颜色
     * 也就是所谓沉浸式状态栏
////     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            finish();
            System.exit(0);

        }
    }



}
