package com.lcpdev.yourweather;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

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



}
